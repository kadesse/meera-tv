# Configurer Firebase pour MEERA TV (gratuit, sans carte bancaire)

Ce guide sert à la fois pour l'**application Android** et le **site MEERA TV
ADMIN**. Firebase remplace complètement le serveur/backend payant — tout
tourne sur l'offre gratuite "Spark" de Google, qui ne demande pas de carte
bancaire et est largement suffisante pour une chaîne comme MEERA TV.

## 1. Créer le projet Firebase

1. Aller sur https://console.firebase.google.com
2. Se connecter avec un compte Google (en créer un gratuitement si besoin)
3. "Ajouter un projet" → nommer le projet, ex. `meera-tv`
4. Désactiver Google Analytics si proposé (pas nécessaire, simplifie la création)
5. Créer le projet — ça prend 30 secondes

## 2. Activer Firestore (la base de données)

1. Dans le menu de gauche : `Build > Firestore Database`
2. "Créer une base de données"
3. Choisir "Mode production" (plus sécurisé)
4. Choisir une région proche (ex. `eur3` pour l'Europe, ou une région Afrique
   si disponible)

## 3. Activer l'authentification (pour la connexion admin)

1. `Build > Authentication` → "Get started"
2. Onglet "Sign-in method" → activer "E-mail/Mot de passe"
3. Onglet "Users" → "Add user" → créer ton compte admin (e-mail + mot de passe)
   → c'est avec ça que tu te connecteras sur le site MEERA TV ADMIN

## 4. Activer Cloud Messaging (notifications, gratuit et illimité)

Rien à faire ici — c'est activé automatiquement. Pour envoyer une
notification plus tard : `Engage > Messaging > Nouvelle campagne` dans la
console Firebase, cibler le topic `meera_all`, écrire le titre/message,
envoyer. Gratuit, aucune limite, pas besoin de backend.

## 5. Récupérer la configuration pour l'application Android

1. Dans les paramètres du projet (icône ⚙️ en haut à gauche) → "Paramètres du projet"
2. Onglet "Général" → section "Vos applications" → cliquer sur l'icône Android
3. Nom du package : `com.meera.tv`
4. Télécharger le fichier `google-services.json`
5. Remplacer le fichier factice `MeeraTV-android/app/google-services.json`
   par celui-ci

## 6. Récupérer la configuration pour le site MEERA TV ADMIN

1. Toujours dans "Vos applications" → cliquer sur l'icône Web `</>`
2. Nom de l'app : `MEERA TV ADMIN`
3. Copier l'objet `firebaseConfig` affiché (ressemble à ceci) :
```js
const firebaseConfig = {
  apiKey: "...",
  authDomain: "meera-tv.firebaseapp.com",
  projectId: "meera-tv",
  storageBucket: "meera-tv.appspot.com",
  messagingSenderId: "...",
  appId: "..."
};
```
4. Coller cet objet dans `meera-admin-web/js/firebase-config.js`
   (voir ce fichier, un emplacement est prévu)

## 7. Déployer les règles de sécurité Firestore

Les règles décrivent qui a le droit de lire/écrire quoi. Le fichier
`meera-admin-web/firestore.rules` les contient déjà, prêtes à l'emploi.

Le plus simple, sans rien installer : dans la console Firebase,
`Build > Firestore Database > Règles`, coller le contenu de
`firestore.rules`, cliquer "Publier".

## 8. Créer les premières données (facultatif, l'admin le fera aussi)

Dans `Firestore Database > Données`, créer une collection `config` avec un
document `liveStatus` contenant :
```
isLive: false (boolean)
title: "" (string)
youtubeVideoId: "" (string)
```
Ce document sera ensuite modifié directement depuis le site admin — cette
étape manuelle n'est là que pour que l'app ne plante pas au tout premier
lancement, avant la première utilisation de l'admin.

## 9. Mettre le site admin en ligne (gratuit)

Le plus simple avec Firebase lui-même (Hosting, gratuit) :
```bash
npm install -g firebase-tools
firebase login
cd meera-admin-web
firebase init hosting   # choisir le projet créé à l'étape 1, dossier public = "."
firebase deploy
```
Une URL gratuite en `https://meera-tv.web.app` est fournie automatiquement.

## Résumé — ce qui est maintenant gratuit et sans serveur à toi

- Base de données (Firestore) : gratuite jusqu'à un usage important, largement au-delà des besoins d'une chaîne comme MEERA TV
- Authentification admin : gratuite, illimitée en petit nombre de comptes
- Notifications : gratuites, illimitées
- Hébergement du site admin : gratuit (Firebase Hosting)
- Vidéos et direct : gratuits et illimités (YouTube)

Aucune carte bancaire n'est demandée pour ce qui précède (offre "Spark").
