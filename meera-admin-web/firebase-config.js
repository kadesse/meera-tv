import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyCe2NsSbrip-IF99Z1fMTkQQncSuKADMuw",
  authDomain: "jks-application.firebaseapp.com",
  projectId: "jks-application",
  storageBucket: "jks-application.firebasestorage.app",
  messagingSenderId: "106262620648",
  appId: "1:106262620648:web:714ea73467d5ee553c5c5d"
};

const app = initializeApp(firebaseConfig);

export const auth = getAuth(app);
export const db = getFirestore(app);