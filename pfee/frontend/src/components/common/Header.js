import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getNotifications, getUnreadNotificationCount, deleteNotification, markAsRead } from '../../services/notification';
import '../styles/header.css';

const Header = () => {
  const { user } = useAuth();
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    if (!user?.token) return undefined;
    loadNotifications();
    const interval = setInterval(loadNotifications, 5000);
    return () => clearInterval(interval);
  }, [user?.token, user?.id]);

  const isNotificationUnread = (n) =>
    n.statut === 'NON_LUE' || (n.lue === false) || (n.statut !== 'LUE' && n.lue !== true);

  const loadNotifications = async () => {
    try {
      const [listRes, countRes] = await Promise.all([
        getNotifications(),
        getUnreadNotificationCount(),
      ]);
      setNotifications(listRes.data || []);
      const count = typeof countRes.data === 'number' ? countRes.data : (listRes.data || []).filter(isNotificationUnread).length;
      setUnreadCount(count);
    } catch (err) {
      console.error('❌ Erreur chargement notifications:', err);
    }
  };

  const handleDeleteNotification = async (id) => {
    console.log('🗑️ CLICK SUPPRIMER - ID:', id);
    try {
      console.log('📤 Envoi requête DELETE à /notifications/' + id);
      const response = await deleteNotification(id);
      console.log('✅ Réponse API:', response.status, response.data);
      console.log('✅ Notification supprimée avec succès');
      setTimeout(() => {
        console.log('🔄 Rafraîchissement de la liste...');
        loadNotifications();
      }, 500);
    } catch (err) {
      console.error('❌ ERREUR suppression:', err.response?.status, err.response?.data || err.message);
    }
  };

  const handleMarkAsRead = async (id) => {
    console.log('📖 CLICK MARQUER LU - ID:', id);
    try {
      console.log('📤 Envoi requête PUT à /notifications/' + id + '/read');
      const response = await markAsRead(id);
      console.log('✅ Réponse API:', response.status, response.data);
      console.log('✅ Notification marquée comme lue');
      setTimeout(() => {
        console.log('🔄 Rafraîchissement de la liste...');
        loadNotifications();
      }, 500);
    } catch (err) {
      console.error('❌ ERREUR marquage:', err.response?.status, err.response?.data || err.message);
    }
  };

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Bonjour';
    if (hour < 18) return 'Bon après-midi';
    return 'Bonsoir';
  };

  const getSubtitle = () => {
    if (user?.role === 'ADMIN') return 'Tableau de bord administrateur';
    if (user?.role === 'CHEF') return 'Tableau de bord chef de parc';
    return 'Tableau de bord';
  };

  return (
    <div className="header">
      <div className="header-left">
        <div className="logo-container">
          <img src="/agilphoto.png" alt="AGIL ENERGY" className="header-logo" />
          <div className="header-title">
            <h1>{getGreeting()}, {user?.prenom || user?.email || 'Utilisateur'} 👋</h1>
            <p className="header-subtitle">{getSubtitle()}</p>
          </div>
        </div>
      </div>
      <div className="header-right">
        <div className="notification-icon-wrapper">
          <button 
            className="notification-btn"
            onClick={() => setShowNotifications(!showNotifications)}
            title="Notifications"
          >
            <span className="notif-bell">🔔</span>
            {unreadCount > 0 && (
              <span className="notif-badge">{unreadCount}</span>
            )}
          </button>

          {showNotifications && (
            <div className="notification-panel">
              <div className="notif-panel-header">
                <h3>📬 Notifications ({notifications.length})</h3>
                <button 
                  className="close-btn"
                  onClick={() => setShowNotifications(false)}
                >
                  ✕
                </button>
              </div>
              <div className="notif-panel-content">
                {notifications.length === 0 ? (
                  <div className="empty-notifications">
                    <p>✓ Aucune notification</p>
                  </div>
                ) : (
                  <div className="notif-list">
                    {notifications.map(notif => (
                      <div key={notif.id} className={`notif-item ${isNotificationUnread(notif) ? 'unread' : 'read'}`}>
                        <div className="notif-header">
                          <span className="notif-title">{notif.titre}</span>
                        </div>
                        <span className="notif-time">
                          {new Date(notif.dateEnvoi).toLocaleTimeString('fr-FR', {
                            hour: '2-digit',
                            minute: '2-digit'
                          })}
                        </span>
                        <p className="notif-message">{notif.message}</p>
                        
                        {notif.missionDestination && (
                          <div className="mission-details">
                            <div className="detail-row">
                              <span className="detail-label">📍 Destination:</span>
                              <span className="detail-value">{notif.missionDestination}</span>
                            </div>
                            {notif.missionDateDebut && (
                              <div className="detail-row">
                                <span className="detail-label">📅 Début:</span>
                                <span className="detail-value">
                                  {new Date(notif.missionDateDebut).toLocaleDateString('fr-FR')}
                                </span>
                              </div>
                            )}
                            {notif.missionDateFin && (
                              <div className="detail-row">
                                <span className="detail-label">📅 Fin:</span>
                                <span className="detail-value">
                                  {new Date(notif.missionDateFin).toLocaleDateString('fr-FR')}
                                </span>
                              </div>
                            )}
                          </div>
                        )}

                        <div className="notif-actions" style={{marginTop: '10px'}}>
                          {isNotificationUnread(notif) && (
                            <button
                              className="notif-action-btn mark-read-btn"
                              onClick={() => {console.log('✓ Click'); handleMarkAsRead(notif.id);}}
                              title="Marquer comme lu"
                              type="button"
                            >
                              ✓ Marquer lu
                            </button>
                          )}
                          <button
                            className="notif-action-btn delete-btn"
                            onClick={() => {console.log('✕ Click'); handleDeleteNotification(notif.id);}}
                            title="Supprimer"
                            type="button"
                          >
                            ✕ Supprimer
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          )}
        </div>

        <div className="date-display">
          {new Date().toLocaleDateString('fr-FR', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric',
          })}
        </div>
      </div>
    </div>
  );
};

export default Header;