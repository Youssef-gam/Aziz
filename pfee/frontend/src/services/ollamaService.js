import axios from 'axios';

const BACKEND_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api';

class OllamaService {
  static async askAssistant(userMessage, context = {}) {
    try {
      const response = await axios.post(
        `${BACKEND_URL}/assistant/ask`,
        { message: userMessage, context },
        { timeout: 30000 }
      );
      return response.data.response || 'Pas de réponse';
    } catch (error) {
      console.error('❌ Erreur assistant:', error);
      throw new Error(error.response?.data?.message || 'Erreur de communication avec l\'assistant IA');
    }
  }

  static async testConnection() {
    try {
      const response = await axios.get(`${BACKEND_URL}/assistant/health`, { timeout: 5000 });
      return response.data?.success === true;
    } catch (error) {
      console.error('❌ Assistant non accessible:', error.message);
      return false;
    }
  }
}

export default OllamaService;
