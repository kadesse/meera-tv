# MEERA TV — Administration

Interface d’administration séparée de l’application Android publique MEERA TV.

## Objectif

Le panneau d’administration permettra au Super Admin de gérer :

- le direct YouTube ;
- les replays ;
- les programmes ;
- les annonces et notifications ;
- les demandes de prière ;
- les paramètres de l’application.

## Sécurité

L’administration doit utiliser Firebase Authentication et des règles Firestore côté serveur.

Aucun mot de passe, clé privée ou identifiant de service ne doit être stocké dans le dépôt public ou dans l’application Android.

## État

Phase initiale : structure de l’administration. Les écrans et l’authentification seront ajoutés progressivement.
