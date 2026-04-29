import React, { useState, useEffect, useCallback } from 'react';
import { getUnreadNotifications, markAsRead, deleteNotification } from '../../services/notification';
import { useNotification } from '../../context/NotificationContext';
import '../../styles/notifications.css';

function NotificationPanel() {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [showPanel, setShowPanel] = useState(false);
  const [loading, setLoading] = useState(false);
  const { showError } = useNotification();

  const loadNotifications = useCallback(async () => {
    try {
      setLoading(true);
      const response = await getUnreadNotifications();
      setNotifications(response.data);
      setUnreadCount(response.data.length);
    } catch (err) {
      console.error('Erreur chargement notifications', err);
      showError('Erreur lors du chargement des notifications');
    } finally {
      setLoading(false);
    }
  }, [showError]);

  useEffect(() => {
    loadNotifications();
    // Recharger toutes les 30 secondes
    const interval = setInterval(loadNotifications, 30000);
    return () => clearInterval(interval);
  }, [loadNotifications]);

  const handleMarkAsRead = async (id) => {
    try {
      await markAsRead(id);
      setNotifications(notifications.filter(n => n.id !== id));
      setUnreadCount(Math.max(0, unreadCount - 1));
    } catch (err) {
      console.error('Erreur marquage notification', err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteNotification(id);
      setNotifications(notifications.filter(n => n.id !== id));
      setUnreadCount(Math.max(0, unreadCount - 1));
    } catch (err) {
      console.error('Erreur suppression notification', err);
    }
  };

  const getNotificationIcon = (type) => {
    const icons = {
      'CONGE_DEMANDE': '📋',
      'CONGE_APPROUVE': '✅',
      'CONGE_REFUSE': '❌',
      'MISSION_ASSIGNEE': '🚗',
      'MISSION_MODIFIEE': '✏️',
      'MISSION_ANNULEE': '🚫',
      'MAINTENANCE_PROGRAMMEE': '🔧',
      'ACCIDENT_DECLARE': '⚠️',
      'AMENDE_DECLAREE': '💰',
      'ALERTE_GENERALE': '🔔'
    };
    return icons[type] || '📢';
  };

  const getNotificationColor = (type) => {
    const colors = {
      'CONGE_DEMANDE': '#FFA500',
      'CONGE_APPROUVE': '#28a745',
      'CONGE_REFUSE': '#dc3545',
      'MISSION_ASSIGNEE': '#007bff',
      'ACCIDENT_DECLARE': '#fd3f0f',
      'AMENDE_DECLAREE': '#ff6b6b'
    };
    return colors[type] || '#6c757d';
  };

  const isBlueNotification = (type) => {
    const blueTypes = ['MISSION_ASSIGNEE', 'MISSION_MODIFIEE', 'MISSION_ANNULEE'];
    return blueTypes.includes(type);
  };

  return (
    <div className="notification-container">
      {/* Bouton bell avec badge */}
      <button 
        className="notification-bell"
        onClick={() => setShowPanel(!showPanel)}
      >
        🔔
        {unreadCount > 0 && (
          <span className="notification-badge">{unreadCount}</span>
        )}
      </button>

      {/* Panneau des notifications */}
      {showPanel && (
        <div className="notification-panel">
          <div className="notification-header">
            <h3>📬 Notifications ({unreadCount})</h3>
            <button onClick={() => setShowPanel(false)} className="close-btn">✕</button>
          </div>

          <div className="notification-content">
            {loading ? (
              <div className="notification-loading">Chargement...</div>
            ) : notifications.length === 0 ? (
              <div className="notification-empty">Aucune notification non lue</div>
            ) : (
              notifications.map(notif => (
                <div 
                  key={notif.id} 
                  className="notification-item"
                  style={{ borderLeftColor: getNotificationColor(notif.type) }}
                >
                  {!isBlueNotification(notif.type) && (
                    <div className="notification-icon">
                      {getNotificationIcon(notif.type)}
                    </div>
                  )}
                  <div className="notification-body">
                    <h4>{notif.titre}</h4>
                    <p>{notif.message}</p>
                    <small className="notification-date">
                      {new Date(notif.dateEnvoi).toLocaleString('fr-FR')}
                    </small>
                  </div>
                  <div className="notification-actions">
                    <button 
                      onClick={() => handleMarkAsRead(notif.id)}
                      className="btn-read"
                      title="Marquer comme lue"
                    >
                      ✓
                    </button>
                    <button 
                      onClick={() => handleDelete(notif.id)}
                      className="btn-delete"
                      title="Supprimer"
                    >
                      ✕
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default NotificationPanel;
