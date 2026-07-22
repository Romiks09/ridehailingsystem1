RideGo — Ride-Hailing System (Capstone)
A JavaFX desktop application for a ride-hailing system (Passenger/Driver roles), backed by a MySQL database (via XAMPP), using Java Serialization for session management, and three GoF design patterns described below.

Design patterns
1. Singleton (Creational) — AppMemory
Only one AppMemory instance ever exists (private constructor + static instance field + getInstance()). Every screen that needs the database goes through that same shared instance, so there's exactly one point of database access for the whole app.

Singleton pattern diagram

2. Facade (Structural) — RideBookingFacade
Booking a ride actually touches two separate subsystems: choosing a pricing algorithm (com.ridehailing.fare) and persisting the ride (AppMemory). BookRideView doesn't need to know about either one individually — it just calls RideBookingFacade.bookRide(...), which coordinates both behind one simple method.

Facade pattern diagram

3. Strategy (Behavioral) — FareStrategy
FareStrategy is an interface with two interchangeable implementations, StandardFareStrategy and PremiumFareStrategy. RideBookingFacade picks one at runtime based on the ride type the passenger selects on the Book a Ride screen. A new pricing scheme (e.g. surge pricing) can be added later as a new class implementing FareStrategy — no existing code has to change.

Strategy pattern diagram

(Diagrams above are included as SVG files in diagrams/. If you want them in the exact app.diagrams.net/draw.io format your instructor asked for, import these SVGs into app.diagrams.net — File → Import — and re-export, or redraw them there directly; they're simple 2–4 box diagrams.)

Software system overview
RideGo lets a Passenger register, log in, book a ride, track their rides, pay for a completed ride, and rate their driver. A Driver registers, logs in, accepts pending ride requests, updates their status, starts/ends a ride, and views their earnings. All data (passengers, drivers, vehicles, rides, payments) is stored in a MySQL database; the app talks to it through JDBC.

Major features
Passenger & Driver registration and login
Ride booking, acceptance, tracking, and completion
Payment recording and driver earnings tracking
Driver rating
Persistent session management via Java Serialization (see below)
Serialization mechanism (session management)
On a successful login, the app builds a small UserSession object (role, user ID, name, phone — no password) and writes it to session.dat using ObjectOutputStream.writeObject(). That file is what the rest of the app checks to know who's logged in:

Creation — PassengerAuthView / DriverAuthView call AppSession.login(...) right after a successful login, which serializes the UserSession to session.dat.
Usage — PassengerDashboardView and DriverDashboardView call AppSession.current() (which deserializes session.dat with ObjectInputStream.readObject()) every time they're shown, and compare the session's user ID against the object being displayed. If the file is missing or doesn't match, the screen redirects back to WelcomeView instead of trusting whatever was passed in-memory — this is what "uses the serialized file to validate and maintain the session while navigating" means in practice.
Deletion — the Logout button on both dashboards calls AppSession.logout(), which deletes session.dat and redirects to the welcome/login screen.
Relevant classes: UserSession (the serializable data), SessionStore (interface), FileSessionStore (the class that actually does the ObjectOutputStream/ObjectInputStream work), and AppSession (the static access point the screens call).

SOLID principles applied
1. Single Responsibility Principle (SRP) AppMemory is responsible for only talking to the database (SQL queries). It knows nothing about JavaFX, buttons, or screens. Every *View class is responsible for only building and wiring up its own screen — it has no SQL in it anywhere. Because these are separate classes, a change to how data is stored (e.g., switching database engines) never requires touching a screen class, and a UI redesign never requires touching AppMemory.

2. Dependency Inversion Principle (DIP) AppSession and every screen that checks the current session (PassengerDashboardView, DriverDashboardView) depend on the SessionStore interface, not on FileSessionStore directly. FileSessionStore (the low-level detail — actual file I/O and serialization) implements that interface. Because the screens only know about the interface, the storage mechanism can be swapped later (e.g., for a database-backed session table) by writing a new class that implements SessionStore — no screen code would need to change.

Benefit of both together: the codebase is split into layers that don't leak into each other (data access / session storage / presentation), so each part can be modified, tested, or replaced independently.

Screens ↔ use cases
Screen	Use case(s) covered
WelcomeView	Entry point — choose role
PassengerAuthView	Register / login (passenger)
PassengerDashboardView	Passenger menu
BookRideView	Book a ride
TrackRideView	Track ride
PaymentView	Make payment
RateDriverView	Rate driver
DriverAuthView	Register / login (driver)
DriverDashboardView	Driver menu
AcceptRideView	Accept ride request, update ride status
EarningsView	View earnings
Sample driver account: phone 0917-111-2222, password 1234.

One-time database setup
See ridehailing_schema.sql. Create a MySQL database in XAMPP/phpMyAdmin and run that script to create the tables. Update src/main/java/com/ridehailing/session/DatabaseConnection.java if your MySQL host, database name, or credentials differ.

How to run
Open in IntelliJ as a Maven project → Maven tab → Plugins → javafx → javafx:run, or right-click Launcher.java → Run.
