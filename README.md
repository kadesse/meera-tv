# MEERA TV — Application Android (version 100% gratuite)

Structure de code réelle (Kotlin + Jetpack Compose) pour l'application
publique MEERA TV. Version gratuite : diffusion via YouTube + données
stockées sur Firebase (offre gratuite Google, sans carte bancaire).

## Ce que contient ce projet

- Écrans : Accueil, Direct, Replays, Programmes, Prédications, Prière,
  Parole de Dieu, Notifications, À propos, Contact.
- Lecture vidéo via YouTube intégré (direct et replays) — gratuit et illimité.
- Firestore (Firebase) pour lire le contenu — aucun serveur à payer.
- Notifications gratuites via Firebase Cloud Messaging (topics).

## 1. Configurer Firebase (obligatoire avant de compiler)

Suivre **FIREBASE-SETUP.md** (fourni avec ce projet), notamment l'étape 5 :
remplacer le fichier factice `app/google-services.json` par le vrai fichier
téléchargé depuis la console Firebase.

## 2. Ouvrir le projet

1. Installer Android Studio : https://developer.android.com/studio
2. `File > Open` → sélectionner le dossier `MeeraTV`
3. Laisser Android Studio régénérer le Gradle Wrapper et synchroniser

## 3. Avant de compiler

- Remplacer l'icône temporaire (`ic_launcher_foreground.xml`) par le vrai
  logo MEERA via `File > New > Image Asset`.
- Vérifier que `app/google-services.json` est bien le vrai fichier Firebase
  (pas le fichier factice fourni par défaut).

## 4. Générer l'APK

- Debug (test rapide) : `Build > Build APK(s)`, ou `./gradlew assembleDebug`
  → `app/build/outputs/apk/debug/app-debug.apk`
- Release (publication) : `Build > Generate Signed Bundle / APK`

## 5. Installer l'APK sur un téléphone

1. `Paramètres > Sécurité > Autoriser les sources inconnues`
2. Transférer `app-debug.apk` (câble, e-mail, WhatsApp...)
3. Ouvrir le fichier sur le téléphone → Installer

## Comment ça marche, en résumé

```
OBS ou app YouTube
   ↓ (diffusion gratuite)
YouTube (hébergement vidéo gratuit et illimité)
   ↓ (ID de la vidéo/direct, collé dans l'admin)
Firestore (Firebase) ── statut du direct, replays, programmes, annonces
   ↓
Application Android (lecture directe, gratuite)
```

Aucun serveur à toi n'est nécessaire nulle part dans ce schéma.

## Versions futures

`versionCode` / `versionName` dans `app/build.gradle.kts`, à incrémenter à
chaque publication de code. Les changements de contenu (replays, programmes,
annonces) sont gérés depuis l'admin et apparaissent automatiquement dans
l'app, sans nouvelle publication.
