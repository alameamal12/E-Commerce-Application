**E-Commerce Application:**
A mobile e-commerce platform built for Android, designed to help a local store owner (Kwesi James) manage and grow online sales. Developed using Android Studio and SQLite as part of a project for BSc Computing.

**Overview:**
This Android application allows users to browse product categories, add products to a cart, place orders, and track order history. Admins can manage products, categories, and users through a secure backend interface.

**Features:**
**User Features:**
- Register and log in with validation
- View all categories and products
- Add/remove products from cart
- Checkout with address input
- View order history and status
- Access account/profile information

**Admin Features:**
- Add, update, delete users
- Add, update, delete categories and products
- View all orders and users
- Assign products to categories
- View detailed order and user information

**Technologies Used:**
- **Java:** Main programming language for the app logic
- **Android Studio:** IDE used for developing and debugging
- **SQLite:** Local database for persisting data
- **Android Jetpack Components:**
  
    - **Room (DAO)** – For structured database access
    - **ViewModel** – Manages UI-related data in a lifecycle-conscious way
    - **LiveData** – Observes data for UI updates
    
- **RecyclerView:** Efficient display of scrollable data lists
- **SharedPreferences:** For storing lightweight user data (e.g., login state)
- **Gradle:** Build automation and dependency management
- **Material Design Components:** For UI consistency and styling

  **Testing:**
- The app underwent black-box testing, covering:
- Valid/invalid login & registration
- Cart operations and checkout
- Admin operations for users/products/categories
- Order creation and tracking
  
**Future Improvements:**
- Integration with cloud storage (Firebase, AWS)
- Payment gateway support
- Push notifications
- Improved UI/UX responsiveness

**Demo:**
- Clone the repository using the following command:
git clone https://github.com/alameamal12/E-Commerce-Application.git
- Open the project in Android Studio.
- Make sure the Android SDK path is correctly set in local.properties.
This is usually generated automatically, but it should look like:
sdk.dir=/Users/your-username/Library/Android/sdk
- Ensure you have the latest Android SDK and Gradle installed.
- Allow Android Studio to sync all dependencies when prompted.
- Click on "Build > Make Project" to compile the code.
- Run the app using an Android emulator or physical device.
- Use the provided demo credentials below to log in as Admin or User.
 
**Demo Credentials:**
| Role  | Username              | Password |
|-------|-----------------------|----------|
| Admin | KwesiJames@gmail.com | admin    |
| User  | lola@gmail.com        | lola2    |
| User  | Shenu23@gmail.com     | Shenu23  |

**Folder Structure:**

- `My_Shop_Final/`  
  - `app/`  
    - `src/`  
      - `main/`  
        - `java/`  
          - `activities/` – App screens like Login, Signup, etc.  
          - `fragments/` – UI fragments for cart, orders, categories, etc.  
          - `models/` – Data models (User, Product, Order, etc.)  
          - `viewmodels/` – Manages UI-related data and logic  
          - `dao/` – Room database DAO interfaces  
          - `utils/` – Helper classes for image handling, preferences, etc.  
          - `adapters/` – RecyclerView and UI adapters  
        - `res/` – Layouts, drawables, values, etc.  
        - `AndroidManifest.xml` – App manifest file  
    - `build/` – Auto-generated build files  
    - `libs/` – External libraries (if any)  
    - `proguard-rules.pro` – Code shrinking and obfuscation rules  
  - `.gradle/` – Gradle build system files  
  - `.idea/` – Android Studio project settings  
  - `gradle/`  
    - `wrapper/` – Gradle wrapper support files  
  - `.gitignore` – Git ignore rules  
  - `build.gradle` – Project-level Gradle config  
  - `settings.gradle` – Project structure config  
  - `gradlew` – Unix Gradle wrapper  
  - `gradlew.bat` – Windows Gradle wrapper  
  - `local.properties` – Local SDK path config (not shared in repo)  
