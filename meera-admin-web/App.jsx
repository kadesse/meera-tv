import React, { useEffect, useState } from "react";
import { onAuthStateChanged, signOut } from "firebase/auth";
import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDoc,
  getDocs,
  setDoc
} from "firebase/firestore";
import { auth, db } from "./firebase-config";
import Login from "./Login";

function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const [page, setPage] = useState("dashboard");

  // DIRECT
  const [isLive, setIsLive] = useState(false);
  const [youtubeVideoId, setYoutubeVideoId] = useState("");
  const [savingLive, setSavingLive] = useState(false);
  const [liveMessage, setLiveMessage] = useState("");

  // REPLAYS
  const [replays, setReplays] = useState([]);
  const [replaysLoading, setReplaysLoading] = useState(false);
  const [replayTitle, setReplayTitle] = useState("");
  const [replayYoutubeId, setReplayYoutubeId] = useState("");
  const [replayDescription, setReplayDescription] = useState("");
  const [savingReplay, setSavingReplay] = useState(false);
  const [replayMessage, setReplayMessage] = useState("");

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      setUser(currentUser);
      setLoading(false);
    });

    return unsubscribe;
  }, []);

  useEffect(() => {
    if (!user) return;

    const loadLiveStatus = async () => {
      try {
        const liveRef = doc(db, "config", "liveStatus");
        const liveSnap = await getDoc(liveRef);

        if (liveSnap.exists()) {
          const data = liveSnap.data();
          setIsLive(data.isLive === true);
          setYoutubeVideoId(data.youtubeVideoId || "");
        }
      } catch (error) {
        console.error(error);
      }
    };

    loadLiveStatus();
  }, [user]);

  const saveLiveStatus = async () => {
    setSavingLive(true);
    setLiveMessage("");

    try {
      await setDoc(doc(db, "config", "liveStatus"), {
        isLive,
        youtubeVideoId: youtubeVideoId.trim(),
        updatedAt: new Date().toISOString()
      });

      setLiveMessage("✅ Direct enregistré avec succès.");
    } catch (error) {
      console.error(error);
      setLiveMessage("❌ Erreur lors de l'enregistrement.");
    } finally {
      setSavingLive(false);
    }
  };

  const loadReplays = async () => {
    setReplaysLoading(true);
    setReplayMessage("");

    try {
      const snapshot = await getDocs(collection(db, "replays"));

      const data = snapshot.docs.map((item) => ({
        id: item.id,
        ...item.data()
      }));

      setReplays(data);
    } catch (error) {
      console.error(error);
      setReplayMessage("❌ Impossible de charger les replays.");
    } finally {
      setReplaysLoading(false);
    }
  };

  const openReplays = async () => {
    setPage("replays");
    await loadReplays();
  };

  const addReplay = async (e) => {
    e.preventDefault();

    if (!replayTitle.trim() || !replayYoutubeId.trim()) {
      setReplayMessage("⚠️ Le titre et l'ID YouTube sont obligatoires.");
      return;
    }

    setSavingReplay(true);
    setReplayMessage("");

    try {
      await addDoc(collection(db, "replays"), {
        title: replayTitle.trim(),
        youtubeVideoId: replayYoutubeId.trim(),
        description: replayDescription.trim(),
        createdAt: new Date().toISOString()
      });

      setReplayTitle("");
      setReplayYoutubeId("");
      setReplayDescription("");

      setReplayMessage("✅ Replay ajouté avec succès.");

      await loadReplays();
    } catch (error) {
      console.error(error);
      setReplayMessage("❌ Erreur lors de l'ajout du replay.");
    } finally {
      setSavingReplay(false);
    }
  };

  const deleteReplay = async (id) => {
    const confirmed = window.confirm(
      "Voulez-vous vraiment supprimer ce replay ?"
    );

    if (!confirmed) return;

    try {
      await deleteDoc(doc(db, "replays", id));
      setReplayMessage("✅ Replay supprimé.");
      await loadReplays();
    } catch (error) {
      console.error(error);
      setReplayMessage("❌ Impossible de supprimer le replay.");
    }
  };

  if (loading) {
    return <p>Chargement...</p>;
  }

  if (!user) {
    return <Login onLogin={() => {}} />;
  }

  return (
    <div className="admin-app">
      <header className="admin-header">
        <div>
          <h1>MEERA TV</h1>
          <p>Administration</p>
        </div>

        <button onClick={() => signOut(auth)}>
          Se déconnecter
        </button>
      </header>

      <main className="dashboard">

        {page === "dashboard" && (
          <>
            <h2>Tableau de bord</h2>
            <p>Bienvenue dans le panneau Super Admin.</p>

            <div className="admin-grid">

              <div
                className="admin-card"
                onClick={() => setPage("direct")}
              >
                <span>📺</span>
                <h3>Direct</h3>
                <p>Gérer le direct YouTube</p>
              </div>

              <div
                className="admin-card"
                onClick={openReplays}
              >
                <span>🎬</span>
                <h3>Replays</h3>
                <p>Gérer les vidéos et replays</p>
              </div>

              <div className="admin-card">
                <span>📅</span>
                <h3>Programmes</h3>
                <p>Gérer les programmes MEERA TV</p>
              </div>

              <div className="admin-card">
                <span>🔔</span>
                <h3>Notifications</h3>
                <p>Publier des annonces</p>
              </div>

              <div className="admin-card">
                <span>🙏</span>
                <h3>Demandes de prière</h3>
                <p>Consulter les demandes</p>
              </div>

              <div className="admin-card">
                <span>⚙️</span>
                <h3>Paramètres</h3>
                <p>Configurer MEERA TV</p>
              </div>

            </div>
          </>
        )}

        {page === "direct" && (
          <div className="live-panel">
            <button onClick={() => setPage("dashboard")}>
              ← Retour
            </button>

            <h2>📺 Gestion du direct</h2>

            <div className="live-status">
              <h3>État du direct</h3>

              <label>
                <input
                  type="checkbox"
                  checked={isLive}
                  onChange={(e) => setIsLive(e.target.checked)}
                />

                {isLive
                  ? " 🟢 Direct actif"
                  : " 🔴 Direct arrêté"}
              </label>
            </div>

            <div className="live-form">
              <label>ID de la vidéo YouTube</label>

              <input
                type="text"
                placeholder="Exemple : dQw4w9WgXcQ"
                value={youtubeVideoId}
                onChange={(e) =>
                  setYoutubeVideoId(e.target.value)
                }
              />

              <button
                onClick={saveLiveStatus}
                disabled={savingLive}
              >
                {savingLive
                  ? "Enregistrement..."
                  : "💾 Enregistrer"}
              </button>

              {liveMessage && <p>{liveMessage}</p>}
            </div>
          </div>
        )}

        {page === "replays" && (
          <div className="live-panel">

            <button onClick={() => setPage("dashboard")}>
              ← Retour
            </button>

            <h2>🎬 Gestion des Replays</h2>

            <h3>Ajouter un replay</h3>

            <form onSubmit={addReplay} className="live-form">

              <label>Titre du replay</label>

              <input
                type="text"
                placeholder="Exemple : Culte de feu"
                value={replayTitle}
                onChange={(e) =>
                  setReplayTitle(e.target.value)
                }
              />

              <label>ID de la vidéo YouTube</label>

              <input
                type="text"
                placeholder="Exemple : dQw4w9WgXcQ"
                value={replayYoutubeId}
                onChange={(e) =>
                  setReplayYoutubeId(e.target.value)
                }
              />

              <label>Description</label>

              <textarea
                placeholder="Description du message"
                value={replayDescription}
                onChange={(e) =>
                  setReplayDescription(e.target.value)
                }
              />

              <button
                type="submit"
                disabled={savingReplay}
              >
                {savingReplay
                  ? "Ajout..."
                  : "➕ Ajouter le replay"}
              </button>

            </form>

            {replayMessage && (
              <p>{replayMessage}</p>
            )}

            <hr />

            <h3>Replays enregistrés</h3>

            {replaysLoading ? (
              <p>Chargement des replays...</p>
            ) : replays.length === 0 ? (
              <p>Aucun replay enregistré.</p>
            ) : (
              <div>
                {replays.map((replay) => (
                  <div
                    key={replay.id}
                    className="admin-card"
                  >
                    <h3>{replay.title}</h3>

                    <p>
                      ID YouTube :{" "}
                      {replay.youtubeVideoId}
                    </p>

                    {replay.description && (
                      <p>{replay.description}</p>
                    )}

                    <button
                      onClick={() =>
                        deleteReplay(replay.id)
                      }
                    >
                      🗑️ Supprimer
                    </button>
                  </div>
                ))}
              </div>
            )}

          </div>
        )}

      </main>
    </div>
  );
}

export default App;