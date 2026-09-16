// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCe2NsSbrip-IF99Z1fMTkQQncSuKADMuw",
  authDomain: "jks-application.firebaseapp.com",
  projectId: "jks-application",
  storageBucket: "jks-application.firebasestorage.app",
  messagingSenderId: "106262620648",
  appId: "1:106262620648:web:714ea73467d5ee553c5c5d",
  measurementId: "G-1E4VV2MLH2"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
