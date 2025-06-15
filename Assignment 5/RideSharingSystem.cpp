#include <iostream>
#include <string>
#include <vector>
#include <memory> // For smart pointers

using namespace std;

// ---------- Base Ride class ----------
class Ride {
protected:
    int rideID;
    string pickupLocation;
    string dropoffLocation;
    double distance; // in kilometers

public:
    Ride(int id, string pickup, string dropoff, double dist)
        : rideID(id), pickupLocation(pickup), dropoffLocation(dropoff), distance(dist) {}

    virtual double calculateFare() const = 0; // Pure virtual for polymorphism

    virtual void rideDetails() const {
        cout << "Ride ID: " << rideID << endl;
        cout << "From: " << pickupLocation << " To: " << dropoffLocation << endl;
        cout << "Distance: " << distance << " km" << endl;
        cout << "Fare: $" << calculateFare() << endl;
    }

    virtual ~Ride() {}
};

// ---------- Derived StandardRide class ----------
class StandardRide : public Ride {
public:
    StandardRide(int id, string pickup, string dropoff, double dist)
        : Ride(id, pickup, dropoff, dist) {}

    double calculateFare() const override {
        return distance * 1.5; // $1.5 per km
    }

    void rideDetails() const override {
        cout << "[Standard Ride]" << endl;
        Ride::rideDetails();
    }
};

// ---------- Derived PremiumRide class ----------
class PremiumRide : public Ride {
public:
    PremiumRide(int id, string pickup, string dropoff, double dist)
        : Ride(id, pickup, dropoff, dist) {}

    double calculateFare() const override {
        return distance * 3.0; // $3.0 per km
    }

    void rideDetails() const override {
        cout << "[Premium Ride]" << endl;
        Ride::rideDetails();
    }
};

// ---------- Driver class ----------
class Driver {
private:
    int driverID;
    string name;
    double rating;
    vector<shared_ptr<Ride>> assignedRides; // Encapsulation: private list

public:
    Driver(int id, string name, double rating)
        : driverID(id), name(name), rating(rating) {}

    void addRide(shared_ptr<Ride> ride) {
        assignedRides.push_back(ride);
    }

    void getDriverInfo() const {
        cout << "\nDriver: " << name << " (ID: " << driverID << "), Rating: " << rating << endl;
        cout << "Assigned Rides:" << endl;
        for (auto& ride : assignedRides) {
            ride->rideDetails();
            cout << "------------------" << endl;
        }
    }
};

// ---------- Rider class ----------
class Rider {
private:
    int riderID;
    string name;
    vector<shared_ptr<Ride>> requestedRides;

public:
    Rider(int id, string name) : riderID(id), name(name) {}

    void requestRide(shared_ptr<Ride> ride) {
        requestedRides.push_back(ride);
    }

    void viewRides() const {
        cout << "\nRider: " << name << " (ID: " << riderID << ")" << endl;
        cout << "Ride History:" << endl;
        for (auto& ride : requestedRides) {
            ride->rideDetails();
            cout << "------------------" << endl;
        }
    }
};

// ---------- Main function ----------
int main() {
    // Create rides using polymorphism
    shared_ptr<Ride> r1 = make_shared<StandardRide>(101, "Downtown", "Uptown", 10.0);
    shared_ptr<Ride> r2 = make_shared<PremiumRide>(102, "Airport", "Hotel", 15.5);

    // Create a driver and assign rides
    Driver driver1(1, "Alice", 4.9);
    driver1.addRide(r1);
    driver1.addRide(r2);

    // Create a rider and request rides
    Rider rider1(1001, "Bob");
    rider1.requestRide(r1);
    rider1.requestRide(r2);

    // Display results
    cout << "\n=== DRIVER INFO ===" << endl;
    driver1.getDriverInfo();

    cout << "\n=== RIDER INFO ===" << endl;
    rider1.viewRides();

    // Demonstrate polymorphism via loop
    cout << "\n=== POLYMORPHIC RIDE LIST ===" << endl;
    vector<shared_ptr<Ride>> rideList = { r1, r2 };
    for (auto& ride : rideList) {
        ride->rideDetails();
        cout << "==================" << endl;
    }

    return 0;
}
