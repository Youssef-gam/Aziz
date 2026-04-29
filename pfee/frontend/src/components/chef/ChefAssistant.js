import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../../styles/chatbot.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api';

const SUGGESTIONS = [
  "Quelles sont les missions d'aujourd'hui ?",
  "Quels véhicules nécessitent une maintenance ?",
  "Affiche les chauffeurs disponibles",
  "Y a-t-il des incidents cette semaine ?",
];

function ChefAssistant({ user }) {
  const [messages, setMessages] = useState([
    {
      id: 1,
      type: 'bot',
      text: "🤖 Bonjour ! Je suis votre assistant IA. Posez-moi une question sur votre parc automobile.",
      timestamp: new Date(),
    },
  ]);
  const [inputValue, setInputValue] = useState('');
  const [loading, setLoading] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const sendMessage = async (text) => {
    if (!text.trim() || loading) return;

    const userMsg = { id: Date.now(), type: 'user', text, timestamp: new Date() };
    setMessages(prev => [...prev, userMsg]);
    setInputValue('');
    setLoading(true);

    try {
      const token = JSON.parse(localStorage.getItem('user') || '{}')?.token;
      const response = await axios.post(
        `${API_URL}/assistant/ask`,
        { message: text, context: {} },
        {
          headers: { Authorization: `Bearer ${token}` },
          timeout: 30000,
        }
      );

      const botMsg = {
        id: Date.now() + 1,
        type: 'bot',
        text: response.data?.response || 'Pas de réponse.',
        timestamp: new Date(),
      };
      setMessages(prev => [...prev, botMsg]);
    } catch (error) {
      const errMsg = {
        id: Date.now() + 1,
        type: 'error',
        text: `❌ Erreur: ${error.response?.data?.message || error.message}`,
        timestamp: new Date(),
      };
      setMessages(prev => [...prev, errMsg]);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    sendMessage(inputValue);
  };

  return (
    <div className="chef-assistant-container">
      <button
        className="assistant-toggle-btn"
        onClick={() => setIsOpen(!isOpen)}
        title="Assistant IA"
      >
        💬
      </button>

      {isOpen && (
        <div className="assistant-window">
          <div className="assistant-header">
            <h3>🤖 Assistant IA - Chef de Parc</h3>
            <button className="close-btn" onClick={() => setIsOpen(false)}>✕</button>
          </div>

          <div className="messages-container">
            {messages.map((msg) => (
              <div key={msg.id} className={`message ${msg.type}`}>
                <div className="message-content">
                  {msg.type === 'user' && <span className="user-icon">👤</span>}
                  {msg.type === 'bot'  && <span className="bot-icon">🤖</span>}
                  {msg.type === 'error' && <span className="error-icon">⚠️</span>}
                  <div className="message-text">{msg.text}</div>
                </div>
                <small className="message-time">
                  {msg.timestamp.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })}
                </small>
              </div>
            ))}

            {loading && (
              <div className="message bot">
                <div className="message-content">
                  <span className="bot-icon">🤖</span>
                  <div className="typing-indicator">
                    <span></span><span></span><span></span>
                  </div>
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>

          <form onSubmit={handleSubmit} className="input-form">
            <input
              type="text"
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              placeholder="Posez une question sur votre parc..."
              disabled={loading}
              className="input-field"
            />
            <button
              type="submit"
              disabled={loading || !inputValue.trim()}
              className="send-btn"
            >
              {loading ? '⏳' : '➤'}
            </button>
          </form>

          <div className="suggestions">
            <small>💡 Exemples:</small>
            <div className="suggestion-chips">
              {SUGGESTIONS.map((s, i) => (
                <button
                  key={i}
                  className="suggestion-chip"
                  onClick={() => sendMessage(s)}
                  disabled={loading}
                >
                  {s}
                </button>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ChefAssistant;
