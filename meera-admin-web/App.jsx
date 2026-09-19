import { useEffect, useState } from "react";
import {
  addDoc,
  collection,
  deleteDoc,
  doc,
  getDoc,
  getDocs,
  setDoc,
  updateDoc,
} from "firebase/firestore";
import {
  onAuthStateChanged,
  signInWithEmailAndPassword,
  signOut,
} from "firebase/auth";

import { db, auth } from "./firebase-config";
import "./style.css";

function App() {
  const [user, setUser] = useState(null);
  const [loadingAuth, setLoadingAuth] = useState(true);

  const [page, setPage] = useState("dashboard");

  const [isLive, setIsLive] = useState(false);
  const [youtubeVideoId, setYoutubeVideoId] = useState("");
  const [liveTitle, setLiveTitle] = useState("");
  const [liveMessage, setLiveMessage] = useState("");
;
  const [replays, setReplays] = useState([]);
  const [replayTitle, setReplayTitle] = useState("");
  const [replayYoutubeId, setReplayYoutubeId] = useState("");
  const [replayDescription, setReplayDescription] = useState("");
  const [replayCategory, setReplayCategory] = useState("ENSEIGNEMENTS");
  const [replayMessage, setReplayMessage] = useState("");

  const [programs, setPrograms] = useState([]);
  const [programsLoading, setProgramsLoading] = useState(false);

  const [programTitle, setProgramTitle] = useState("");
  const [programDescription, setProgramDescription] = useState("");
  const [programImageUrl, setProgramImageUrl] = useState("");
  const [programCategory, setProgramCategory] = useState("CULTE");
  const [programStartTime, setProgramStartTime] = useState("");
  const [programEndTime, setProgramEndTime] = useState("");

  const [editingProgramId, setEditingProgramId] = useState(null);
  const [savingProgram, setSavingProgram] = useState(false);
  const [programMessage, setProgramMessage] = useState("")

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loginError, setLoginError] = useState("");
const [prayerRequests, setPrayerRequests] = useState([]);
const [prayerLoading, setPrayerLoading] = useState(false);
const [prayerMessage, setPrayerMessage] = useState("");
const [announcements, setAnnouncements] = useState([]);
const [announcementTitle, setAnnouncementTitle] = useState("");
const [announcementBody, setAnnouncementBody] = useState("");
const [announcementImageUrl, setAnnouncementImageUrl] = useState("");
const [announcementMessage, setAnnouncementMessage] = useState("");
const [announcementLoading, setAnnouncementLoading] = useState(false);
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      setUser(currentUser);
      setLoadingAuth(false);
    });

    return unsubscribe;
  }, []);

  useEffect(() => {
    if (user) {
      loadLive();
      loadReplays();
    }
  }, [user]);

  async function loadLive() {
    try {
      const snap = await getDoc(doc(db, "config", "liveStatus"));

      if (snap.exists()) {
        const data = snap.data();

        setIsLive(Boolean(data.isLive));
        setYoutubeVideoId(data.youtubeVideoId || "");
        setLiveTitle(data.title || "");
      }
    } catch (error) {
      console.error(error);
    }
  }

  async function saveLive() {
    try {
      await setDoc(doc(db, "config", "liveStatus"), {
        isLive,
        youtubeVideoId: youtubeVideoId.trim(),
        title: liveTitle.trim(),
        updatedAt: new Date().toISOString(),
      });

      setLiveMessage("✅ Direct enregistré avec succès.");
    } catch (error) {
      console.error(error);
      setLiveMessage("❌ Erreur lors de l'enregistrement.");
    }
  }

  async function loadReplays() {
    try {
      const snapshot = await getDocs(collection(db, "replays"));

      const data = snapshot.docs.map((item) => ({
        id: item.id,
        ...item.data(),
      }));

      data.sort((a, b) => {
        const dateA = new Date(a.date || a.createdAt || 0).getTime();
        const dateB = new Date(b.date || b.createdAt || 0).getTime();
        return dateB - dateA;
      });

      setReplays(data);
    } catch (error) {
      console.error(error);
    }
  }

  async function addReplay() {
    if (!replayTitle.trim() || !replayYoutubeId.trim()) {
      setReplayMessage("⚠️ Le titre et l'ID YouTube sont obligatoires.");
      return;
    }

    try {
      await addDoc(collection(db, "replays"), {
        title: replayTitle.trim(),
        youtubeVideoId: replayYoutubeId.trim(),
        description: replayDescription.trim(),
        category: replayCategory,
        date: new Date().toISOString(),
        createdAt: new Date().toISOString(),
      });

      setReplayTitle("");
      setReplayYoutubeId("");
      setReplayDescription("");
      setReplayCategory("ENSEIGNEMENTS");

      setReplayMessage("✅ Replay ajouté avec succès.");

      await loadReplays();
    } catch (error) {
      console.error(error);
      setReplayMessage("❌ Erreur lors de l'ajout du replay.");
    }
  }

  async function deleteReplay(id) {
    const confirmation = window.confirm(
      "Voulez-vous vraiment supprimer ce replay ?"
    );

    if (!confirmation) return;

    try {
      await deleteDoc(doc(db, "replays", id));
      await loadReplays();
    } catch (error) {
      console.error(error);
      alert("Erreur lors de la suppression.");
    }
  }

  async function loadPrograms() {
    setProgramsLoading(true);

    try {
      const snapshot = await getDocs(collection(db, "programs"));

      const data = snapshot.docs.map((item) => ({
        id: item.id,
        ...item.data(),
      }));

      data.sort((a, b) => {
        const dateA = new Date(a.startTime || 0).getTime();
        const dateB = new Date(b.startTime || 0).getTime();
        return dateA - dateB;
      });

      setPrograms(data);
    } catch (error) {
      console.error(error);
      setProgramMessage("❌ Impossible de charger les programmes.");
    } finally {
      setProgramsLoading(false);
    }
  }

  function openPrograms() {
    setPage("programs");
    loadPrograms();
  }

async function loadPrayerRequests() {
  setPrayerLoading(true);

  try {
    const snapshot = await getDocs(
      collection(db, "prayerRequests")
    );

    const data = snapshot.docs.map((item) => ({
      id: item.id,
      ...item.data(),
    }));

    data.sort((a, b) => {
      const dateA = new Date(a.createdAt || 0).getTime();
      const dateB = new Date(b.createdAt || 0).getTime();
      return dateB - dateA;
    });

    setPrayerRequests(data);
  } catch (error) {
    console.error(error);
    setPrayerMessage(
      "❌ Impossible de charger les demandes de prière."
    );
  } finally {
    setPrayerLoading(false);
  }
}

async function deletePrayerRequest(id) {
  const confirmation = window.confirm(
    "Voulez-vous vraiment supprimer cette demande de prière ?"
  );

  if (!confirmation) return;

  try {
    await deleteDoc(doc(db, "prayerRequests", id));
    await loadPrayerRequests();
    setPrayerMessage("✅ Demande supprimée.");
  } catch (error) {
    console.error(error);
    setPrayerMessage(
      "❌ Erreur lors de la suppression."
    );
  }
}  
function resetProgramForm() {
    setProgramTitle("");
    setProgramDescription("");
    setProgramImageUrl("");
    setProgramCategory("CULTE");
    setProgramStartTime("");
    setProgramEndTime("");
    setEditingProgramId(null);
    setProgramMessage("");
  }

  async function saveProgram() {
    if (
      !programTitle.trim() ||
      !programStartTime ||
      !programEndTime
    ) {
      setProgramMessage(
        "⚠️ Le titre, la date/heure de début et la date/heure de fin sont obligatoires."
      );
      return;
    }

    if (
      new Date(programEndTime).getTime() <=
      new Date(programStartTime).getTime()
    ) {
      setProgramMessage(
        "⚠️ L'heure de fin doit être après l'heure de début."
      );
      return;
    }

    setSavingProgram(true);
    setProgramMessage("");

    const data = {
      title: programTitle.trim(),
      description: programDescription.trim(),
      imageUrl: programImageUrl.trim(),
      category: programCategory,
      startTime: new Date(programStartTime).toISOString(),
      endTime: new Date(programEndTime).toISOString(),
      updatedAt: new Date().toISOString(),
    };

    try {
      if (editingProgramId) {
        await updateDoc(
          doc(db, "programs", editingProgramId),
          data
        );

        setProgramMessage("✅ Programme modifié avec succès.");
      } else {
        await addDoc(collection(db, "programs"), {
          ...data,
          createdAt: new Date().toISOString(),
        });

        setProgramMessage("✅ Programme ajouté avec succès.");
      }

      resetProgramForm();
      await loadPrograms();
    } catch (error) {
      console.error(error);
      setProgramMessage(
        "❌ Erreur lors de l'enregistrement du programme."
      );
    } finally {
      setSavingProgram(false);
    }
  }

  function editProgram(program) {
    setEditingProgramId(program.id);
    setProgramTitle(program.title || "");
    setProgramDescription(program.description || "");
    setProgramImageUrl(program.imageUrl || "");
    setProgramCategory(program.category || "CULTE");

    if (program.startTime) {
      setProgramStartTime(toDateTimeLocal(program.startTime));
    }

    if (program.endTime) {
      setProgramEndTime(toDateTimeLocal(program.endTime));
    }

    setProgramMessage("");
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  async function deleteProgram(id) {
    const confirmation = window.confirm(
      "Voulez-vous vraiment supprimer ce programme ?"
    );

    if (!confirmation) return;

    try {
      await deleteDoc(doc(db, "programs", id));

      if (editingProgramId === id) {
        resetProgramForm();
      }

      await loadPrograms();
      setProgramMessage("✅ Programme supprimé.");
    } catch (error) {
      console.error(error);
      setProgramMessage(
        "❌ Erreur lors de la suppression du programme."
      );
    }
  }

 async function loadAnnouncements() {
  setAnnouncementLoading(true);

  try {
    const snapshot = await getDocs(
      collection(db, "announcements")
    );

    const data = snapshot.docs.map((item) => ({
      id: item.id,
      ...item.data(),
    }));

    data.sort((a, b) => {
      const dateA = new Date(a.createdAt || 0).getTime();
      const dateB = new Date(b.createdAt || 0).getTime();
      return dateB - dateA;
    });

    setAnnouncements(data);
  } catch (error) {
    console.error(error);
    setAnnouncementMessage(
      "❌ Impossible de charger les annonces."
    );
  } finally {
    setAnnouncementLoading(false);
  }
}

async function addAnnouncement() {
  if (!announcementTitle.trim() || !announcementBody.trim()) {
    setAnnouncementMessage(
      "⚠️ Le titre et le message sont obligatoires."
    );
    return;
  }

  try {
    await addDoc(collection(db, "announcements"), {
      title: announcementTitle.trim(),
      body: announcementBody.trim(),
      imageUrl: announcementImageUrl.trim(),
      createdAt: new Date().toISOString(),
    });

    setAnnouncementTitle("");
    setAnnouncementBody("");
    setAnnouncementImageUrl("");

    setAnnouncementMessage(
      "✅ Annonce publiée avec succès."
    );

    await loadAnnouncements();
  } catch (error) {
    console.error(error);
    setAnnouncementMessage(
      "❌ Erreur lors de la publication."
    );
  }
}

async function deleteAnnouncement(id) {
  const confirmation = window.confirm(
    "Voulez-vous vraiment supprimer cette annonce ?"
  );

  if (!confirmation) return;

  try {
    await deleteDoc(doc(db, "announcements", id));
    await loadAnnouncements();

    setAnnouncementMessage(
      "✅ Annonce supprimée."
    );
  } catch (error) {
    console.error(error);
    setAnnouncementMessage(
      "❌ Erreur lors de la suppression."
    );
  }
} 
function toDateTimeLocal(value) {
    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return "";
    }

    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60000);

    return localDate.toISOString().slice(0, 16);
  }

  async function login(event) {
    event.preventDefault();
    setLoginError("");

    try {
      await signInWithEmailAndPassword(
        auth,
        email,
        password
      );
    } catch (error) {
      console.error(error);
      setLoginError(
        "❌ Email ou mot de passe incorrect."
      );
    }
  }

  async function logout() {
    await signOut(auth);
  }

  if (loadingAuth) {
    return (
      <div className="login-page">
        <div className="login-box">
          <h1>J-C TV</h1>
          <p>Chargement...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="login-page">
        <form className="login-box" onSubmit={login}>
          <h1>J-C TV</h1>
          <h2>Administration</h2>

          <input
            type="email"
            placeholder="Adresse e-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <input
            type="password"
            placeholder="Mot de passe"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <button type="submit">
            Se connecter
          </button>

          {loginError && (
            <p className="error">{loginError}</p>
          )}
        </form>
      </div>
    );
  }

  return (
    <div className="admin-app">
      <header className="admin-header">
        <div>
          <h1>J-C TV</h1>
          <p>Administration</p>
        </div>

        <button onClick={logout}>
          Déconnexion
        </button>
      </header>

      <main className="admin-main">
        {page === "dashboard" && (
          <>
            <h2>Tableau de bord</h2>

            <div className="admin-grid">
              <button
                className="admin-card"
                onClick={() => setPage("live")}
              >
                <span>🔴</span>
                <h3>Direct</h3>
                <p>Gérer le direct J-C TV</p>
              </button>

              <button
                className="admin-card"
                onClick={() => openPrograms()}
              >
                <span>📅</span>
                <h3>Programmes</h3>
                <p>Gérer les programmes J-C TV</p>
              </button>

              <button
                className="admin-card"
                onClick={() => setPage("replays")}
              >
                <span>🎬</span>
                <h3>Replays</h3>
                <p>Gérer les vidéos disponibles</p>
              </button>

              
                <button
  className="admin-card"
  onClick={() => {
    setPage("prayers");
    loadPrayerRequests();
  }}
>
  <span>🙏</span>
  <h3>Prières</h3>
  <p>Demandes de prière</p>
</button>

              <button
  className="admin-card"
  onClick={() => {
    setPage("announcements");
    loadAnnouncements();
  }}
>
  <span>📢</span>
  <h3>Annonces</h3>
  <p>Communications J-C TV</p>
</button>            </div>
          </>
        )}

        {page === "live" && (
          <section>
            <button
              className="back-button"
              onClick={() => setPage("dashboard")}
            >
              ← Retour
            </button>

            <h2>🔴 Gestion du direct</h2>

            <div className="admin-panel">
              <label>
                <input
                  type="checkbox"
                  checked={isLive}
                  onChange={(e) => setIsLive(e.target.checked)}
                />
                <strong> DIRECT ACTIF</strong>
              </label>

              <label>
                Titre du direct
                <input
                  type="text"
                  placeholder="Exemple : Culte de feu"
                  value={liveTitle}
                  onChange={(e) =>
                    setLiveTitle(e.target.value)
                  }
                />
              </label>

              <label>
                ID de la vidéo YouTube
                <input
                  type="text"
                  placeholder="Exemple : ABC123xyz"
                  value={youtubeVideoId}
                  onChange={(e) =>
                    setYoutubeVideoId(e.target.value)
                  }
                />
              </label>

              <button onClick={saveLive}>
                💾 Enregistrer le direct
              </button>

              {liveMessage && (
                <p>{liveMessage}</p>
              )}
            </div>
          </section>
        )}

        {page === "replays" && (
          <section>
            <button
              className="back-button"
              onClick={() => setPage("dashboard")}
            >
              ← Retour
            </button>

            <h2>🎬 Gestion des replays</h2>

            <div className="admin-panel">
              <h3>Ajouter un replay</h3>

              <label>
                Titre
                <input
                  type="text"
                  value={replayTitle}
                  onChange={(e) =>
                    setReplayTitle(e.target.value)
                  }
                />
              </label>

              <label>
                ID YouTube
                <input
                  type="text"
                  value={replayYoutubeId}
                  onChange={(e) =>
                    setReplayYoutubeId(e.target.value)
                  }
                />
              </label>

              <label>
                Catégorie
                <select
                  value={replayCategory}
                  onChange={(e) =>
                    setReplayCategory(e.target.value)
                  }
                >
                  <option value="CULTES">Cultes</option>
                  <option value="PREDICATIONS">
                    Prédications
                  </option>
                  <option value="PRIERES">Prières</option>
                  <option value="ENSEIGNEMENTS">
                    Enseignements
                  </option>
                  <option value="TEMOIGNAGES">
                    Témoignages
                  </option>
                  <option value="EVANGELISATION">
                    Évangélisation
                  </option>
                  <option value="EMISSIONS">
                    Émissions
                  </option>
                </select>
              </label>

              <label>
                Description
                <textarea
                  value={replayDescription}
                  onChange={(e) =>
                    setReplayDescription(e.target.value)
                  }
                />
              </label>

              <button onClick={addReplay}>
                ➕ Ajouter le replay
              </button>

              {replayMessage && (
                <p>{replayMessage}</p>
              )}
            </div>

            <div className="admin-panel">
              <h3>Replays enregistrés</h3>

              {replays.length === 0 ? (
                <p>Aucun replay.</p>
              ) : (
                replays.map((replay) => (
                  <div
                    key={replay.id}
                    className="admin-list-item"
                  >
                    <div>
                      <strong>
                        {replay.title}
                      </strong>

                      <p>
                        {replay.category ||
                          "Sans catégorie"}
                      </p>
                    </div>

                    <button
                      onClick={() =>
                        deleteReplay(replay.id)
                      }
                    >
                      🗑️
                    </button>
                  </div>
                ))
              )}
            </div>
          </section>
        )}

      {page === "announcements" && (
  <section>
    <button
      className="back-button"
      onClick={() => setPage("dashboard")}
    >
      ← Retour
    </button>

    <h2>📢 Annonces J-C TV</h2>

    <div className="admin-panel">
      <h3>➕ Publier une annonce</h3>

      <label>
        Titre
        <input
          type="text"
          placeholder="Exemple : Grande soirée de prière"
          value={announcementTitle}
          onChange={(e) =>
            setAnnouncementTitle(e.target.value)
          }
        />
      </label>

      <label>
        Message
        <textarea
          placeholder="Écrivez le contenu de l'annonce..."
          value={announcementBody}
          onChange={(e) =>
            setAnnouncementBody(e.target.value)
          }
        />
      </label>

      <label>
        Image — URL facultative
        <input
          type="text"
          placeholder="https://..."
          value={announcementImageUrl}
          onChange={(e) =>
            setAnnouncementImageUrl(e.target.value)
          }
        />
      </label>

      <button onClick={addAnnouncement}>
        📢 Publier l'annonce
      </button>

      {announcementMessage && (
        <p>{announcementMessage}</p>
      )}
    </div>

    <div className="admin-panel">
      <h3>📋 Annonces publiées</h3>

      {announcementLoading ? (
        <p>Chargement...</p>
      ) : announcements.length === 0 ? (
        <p>Aucune annonce publiée.</p>
      ) : (
        announcements.map((announcement) => (
          <div
            key={announcement.id}
            className="admin-list-item"
          >
            <div>
              {announcement.imageUrl && (
                <img
                  src={announcement.imageUrl}
                  alt=""
                  style={{
                    width: "120px",
                    height: "70px",
                    objectFit: "cover",
                    borderRadius: "8px",
                    marginBottom: "8px",
                  }}
                />
              )}

              <strong>
                {announcement.title}
              </strong>

              <p>{announcement.body}</p>

              {announcement.createdAt && (
                <p>
                  📅{" "}
                  {formatProgramDate(
                    announcement.createdAt
                  )}
                </p>
              )}
            </div>

            <button
              onClick={() =>
                deleteAnnouncement(announcement.id)
              }
            >
              🗑️
            </button>
          </div>
        ))
      )}
    </div>
  </section>
)} 
{page === "prayers" && (
  <section>
    <button
      className="back-button"
      onClick={() => setPage("dashboard")}
    >
      ← Retour
    </button>

    <h2>🙏 Demandes de prière</h2>

    <div className="admin-panel">
      {prayerMessage && <p>{prayerMessage}</p>}

      {prayerLoading ? (
        <p>Chargement des demandes...</p>
      ) : prayerRequests.length === 0 ? (
        <p>Aucune demande de prière pour le moment.</p>
      ) : (
        prayerRequests.map((request) => (
          <div
            key={request.id}
            className="admin-list-item"
          >
            <div>
              <strong>
                {request.name || "Anonyme"}
              </strong>

              {request.phoneOrEmail && (
                <p>
                  📞 {request.phoneOrEmail}
                </p>
              )}

              <p>
                📝 {request.message}
              </p>

              {request.createdAt && (
                <p>
                  📅 {formatProgramDate(request.createdAt)}
                </p>
              )}
            </div>

            <button
              onClick={() =>
                deletePrayerRequest(request.id)
              }
            >
              🗑️
            </button>
          </div>
        ))
      )}
    </div>
  </section>
)} 
{page === "programs" && (
          <section>
            <button
              className="back-button"
              onClick={() => setPage("dashboard")}
            >
              ← Retour
            </button>

            <h2>📅 Gestion des programmes</h2>

            <div className="admin-panel">
              <h3>
                {editingProgramId
                  ? "✏️ Modifier le programme"
                  : "➕ Ajouter un programme"}
              </h3>

              <label>
                Titre du programme
                <input
                  type="text"
                  placeholder="Exemple : Culte de feu"
                  value={programTitle}
                  onChange={(e) =>
                    setProgramTitle(e.target.value)
                  }
                />
              </label>

              <label>
                Catégorie
                <select
                  value={programCategory}
                  onChange={(e) =>
                    setProgramCategory(e.target.value)
                  }
                >
                  <option value="CULTE">Culte</option>
                  <option value="PRIERE">Prière</option>
                  <option value="ENSEIGNEMENT">
                    Enseignement
                  </option>
                  <option value="EVANGELISATION">
                    Évangélisation
                  </option>
                  <option value="JEUNESSE">
                    Jeunesse
                  </option>
                  <option value="AUTRE">Autre</option>
                </select>
              </label>

              <label>
                Description
                <textarea
                  placeholder="Description du programme"
                  value={programDescription}
                  onChange={(e) =>
                    setProgramDescription(e.target.value)
                  }
                />
              </label>

              <label>
                Image du programme — URL
                <input
                  type="text"
                  placeholder="https://..."
                  value={programImageUrl}
                  onChange={(e) =>
                    setProgramImageUrl(e.target.value)
                  }
                />
              </label>

              <label>
                Date et heure de début
                <input
                  type="datetime-local"
                  value={programStartTime}
                  onChange={(e) =>
                    setProgramStartTime(e.target.value)
                  }
                />
              </label>

              <label>
                Date et heure de fin
                <input
                  type="datetime-local"
                  value={programEndTime}
                  onChange={(e) =>
                    setProgramEndTime(e.target.value)
                  }
                />
              </label>

              <div className="button-row">
                <button
                  onClick={saveProgram}
                  disabled={savingProgram}
                >
                  {savingProgram
                    ? "Enregistrement..."
                    : editingProgramId
                    ? "💾 Modifier"
                    : "➕ Ajouter"}
                </button>

                {editingProgramId && (
                  <button
                    type="button"
                    onClick={resetProgramForm}
                  >
                    Annuler
                  </button>
                )}
              </div>

              {programMessage && (
                <p>{programMessage}</p>
              )}
            </div>

            <div className="admin-panel">
              <h3>Programmes enregistrés</h3>

              {programsLoading ? (
                <p>Chargement...</p>
              ) : programs.length === 0 ? (
                <p>Aucun programme enregistré.</p>
              ) : (
                programs.map((program) => (
                  <div
                    key={program.id}
                    className="admin-list-item"
                  >
                    <div>
                      {program.imageUrl && (
                        <img
                          src={program.imageUrl}
                          alt=""
                          style={{
                            width: "90px",
                            height: "60px",
                            objectFit: "cover",
                            borderRadius: "8px",
                            marginBottom: "8px",
                          }}
                        />
                      )}

                      <strong>
                        {program.title}
                      </strong>

                      <p>
                        {program.category ||
                          "Autre"}
                      </p>

                      <p>
                        {formatProgramDate(
                          program.startTime
                        )}
                        {" → "}
                        {formatProgramDate(
                          program.endTime
                        )}
                      </p>

                      {program.description && (
                        <p>
                          {program.description}
                        </p>
                      )}
                    </div>

                    <div className="button-row">
                      <button
                        onClick={() =>
                          editProgram(program)
                        }
                      >
                        ✏️
                      </button>

                      <button
                        onClick={() =>
                          deleteProgram(program.id)
                        }
                      >
                        🗑️
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </section>
        )}
      </main>
    </div>
  );
}

function formatProgramDate(value) {
  if (!value) return "";

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return date.toLocaleString("fr-FR", {
    dateStyle: "short",
    timeStyle: "short",
  });
}

export default App;