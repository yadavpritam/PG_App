# PG App

<div align="center">

## Modern PG Discovery & Listing Android Application

A fully modern Android application built using **Kotlin**, **Jetpack Compose**, **Firebase**, and **Google Maps** that allows users to discover, explore, and list PG (Paying Guest) accommodations through an interactive map-based experience.

---

### Built With

Kotlin • Jetpack Compose • Firebase • Google Maps • MVVM • Hilt

</div>

---

# Overview

PG App is designed to provide a smooth and modern accommodation discovery experience where users can easily explore nearby PGs, view listings on Google Maps, search based on location or budget, and navigate directly to selected PGs.

The project follows modern Android development practices with scalable architecture, clean UI implementation, and real-time location support.

---

# Features

## Authentication System
- Firebase Authentication
- User Login & Registration
- Persistent User Session

## Interactive Map Experience
- Google Maps Integration
- Custom PG Markers
- Current User Location
- Smooth Camera Animations
- Interactive Marker Info Windows

## PG Listing System
Users can add PGs with:
- PG Name
- Location
- Price
- Availability Status
- Images
- Latitude & Longitude

## Search & Filtering
- Search by PG name
- Search by area/location
- Price-based filtering
- Dynamic real-time filtering

## Navigation Support
- Direct Google Maps Navigation
- Distance calculation from user location
- Route opening support

## Modern UI/UX
- Jetpack Compose UI
- Material 3 Design
- Responsive Layouts
- Animated Components
- Expandable Bottom Sections
- Professional Card Design

## Additional Screens
- Splash Screen
- Authentication Screen
- Explore Screen
- Add PG Screen
- PG Detail Screen
- Saved Screen
- Profile Screen
- Filter Screen

---

# Screenshots

# Demo Video

Watch App Demo Here:

https://drive.google.com/file/d/1o2T27k7iqfrO5OcHaac_dC070SFEWknT/view?usp=sharing

> Add your application screenshots here

```text
screenshots/
│
├── splash.png
├── auth.png
├── explore.png
├── details.png
├── add_pg.png
└── profile.png
```

---

# Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI Toolkit | Jetpack Compose |
| Architecture | MVVM |
| Dependency Injection | Hilt |
| Backend | Firebase |
| Database | Firebase Firestore |
| Authentication | Firebase Auth |
| Maps | Google Maps Compose |
| Location Services | Fused Location Provider |
| Navigation | Navigation Compose |
| Image Loading | Coil |

---

# Architecture

The application follows a clean MVVM architecture to maintain scalability, readability, and separation of concerns.

```text
UI Layer (Compose Screens)
        ↓
ViewModel Layer
        ↓
Repository Layer
        ↓
Firebase / APIs / Location Services
```

---

# Project Structure

```text
com.example.pgapp
│
├── data
│   ├── model
│   ├── repository
│   └── remote
│
├── navigation
│
├── ui
│   ├── screens
│   ├── components
│   └── theme
│
├── utils
│
├── viewmodel
│
└── MainActivity
```

---

# Libraries Used

```gradle
// Navigation
implementation("androidx.navigation:navigation-compose")

// Hilt
implementation("com.google.dagger:hilt-android")
ksp("com.google.dagger:hilt-compiler")

// Firebase
implementation(platform("com.google.firebase:firebase-bom"))
implementation("com.google.firebase:firebase-auth")
implementation("com.google.firebase:firebase-firestore")

// Maps & Location
implementation("com.google.maps.android:maps-compose")
implementation("com.google.android.gms:play-services-maps")
implementation("com.google.android.gms:play-services-location")

// Image Loading
implementation("io.coil-kt:coil-compose")
```

---

# Key Functionalities

## Explore Nearby PGs
Users can discover nearby PG accommodations directly on an interactive map with real-time markers and smooth camera positioning.

## Smart Search & Filtering
The app provides dynamic searching and filtering features for finding PGs faster based on:
- Location
- Budget
- PG Name

## Real-Time Location Handling
The application automatically fetches and updates the user’s current location for better navigation and nearby recommendations.

## Google Maps Navigation
Integrated Google Maps navigation allows users to directly navigate to selected PGs.

## Dynamic Bottom PG Section
A modern expandable bottom section provides a seamless browsing experience without leaving the map.

---

# Setup Instructions

## 1. Clone Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_LINK
```

---

## 2. Open Project

Open the project in **Android Studio**.

---

## 3. Add Google Maps API Key

Inside `AndroidManifest.xml`

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY" />
```

---

## 4. Setup Firebase

- Add `google-services.json`
- Enable Firebase Authentication
- Enable Firebase Firestore

---

## 5. Build & Run

Run the project on:
- Physical Android Device
- Android Emulator

---

# Future Improvements

- PG Booking System
- In-App Chat
- Push Notifications
- Dark Mode
- Reviews & Ratings
- Realtime Availability
- Advanced Filters
- AI-based Recommendations
- Admin Panel

---

# Learning Outcomes

This project demonstrates practical implementation of:

- Modern Android Development
- Jetpack Compose
- MVVM Architecture
- Firebase Integration
- Google Maps SDK
- Location Services
- State Management
- Hilt Dependency Injection
- Professional UI/UX Design

---

# Author

## Pritam Yadav

Android Developer passionate about building modern, scalable, and user-friendly Android applications using Kotlin and Jetpack Compose.

- Kotlin Development
- Jetpack Compose
- Firebase
- Google Maps Integration
- Modern Android Architecture

---

<div align="center">

### If you found this project helpful, consider giving it a star.

</div>
