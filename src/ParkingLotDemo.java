import java.util.*;                 // gives us List, Map, HashMap, ArrayList
import java.time.LocalDateTime;     // lets us store an exact date+time
import java.time.Duration;          // lets us calculate time difference between two LocalDateTimes

// ===================================================================
// ENUMS — a fixed list of named values (like a dropdown with fixed options)
// ===================================================================

enum VehicleType {
    BIKE,   // one fixed option
    CAR,    // another fixed option
    BUS     // another fixed option
}
// Why enum and not String? Because "Bike"/"bike"/"BIKE" typos are impossible now.
// The compiler only allows these 3 exact values.

enum SlotType {
    SMALL,
    MEDIUM,
    LARGE
}
// Same idea — fixed slot sizes.

// ===================================================================
// VEHICLE — represents any vehicle (Bike/Car/Bus)
// ===================================================================

abstract class Vehicle {
    // "abstract" means: you can NEVER write "new Vehicle(...)" directly.
    // You can only create a Bike, Car, or Bus (the subclasses below).

    private String vehicleNumber;  // e.g. "TN59AB1234" — private = only this class can touch it directly
    private VehicleType type;      // BIKE / CAR / BUS

    // Constructor: runs once, when a Vehicle object is first created.
    public Vehicle(String vehicleNumber, VehicleType type) {
        this.vehicleNumber = vehicleNumber; // "this.vehicleNumber" = the field; "vehicleNumber" = the parameter passed in
        this.type = type;
    }

    // Getter methods: since fields are private, other classes must use these
    // methods to READ the values (they still can't directly change them).
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getType() {
        return type;
    }
}

// Bike "extends" Vehicle -> Bike IS-A Vehicle (inheritance).
// It automatically gets vehicleNumber/type/getters from Vehicle for free.
class Bike extends Vehicle {
    public Bike(String vehicleNumber) {
        // "super(...)" calls the PARENT class's (Vehicle's) constructor.
        // We pass BIKE automatically, so whoever creates a Bike doesn't need to say the type.
        super(vehicleNumber, VehicleType.BIKE);
    }
}

class Car extends Vehicle {
    public Car(String vehicleNumber) {
        super(vehicleNumber, VehicleType.CAR);
    }
}

class Bus extends Vehicle {
    public Bus(String vehicleNumber) {
        super(vehicleNumber, VehicleType.BUS);
    }
}

// ===================================================================
// TICKET — the "receipt" created when a vehicle parks
// ===================================================================

class Ticket {
    private String ticketId;             // unique ID, e.g. "T-1"
    private String vehicleNumber;        // whose ticket this is
    private VehicleType vehicleType;     // needed later to know which hourly rate to charge
    private LocalDateTime entryTime;     // when the vehicle came in
    private LocalDateTime exitTime;      // when it left (empty/null until it actually leaves)
    private double parkingFee;           // amount to pay (0 until calculated at exit)

    public Ticket(String ticketId, String vehicleNumber, VehicleType vehicleType) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.entryTime = LocalDateTime.now();  // "now" = the exact moment this Ticket object is created
        this.exitTime = null;                  // no exit yet, so we leave it empty
        this.parkingFee = 0.0;                 // no fee yet, since vehicle hasn't left
    }

    // Getters — read-only access from outside this class
    public String getTicketId() { return ticketId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getParkingFee() { return parkingFee; }

    // Setters — ONLY for the 2 fields that are unknown at creation time
    // and get filled in later, when the vehicle exits.
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setParkingFee(double parkingFee) {
        this.parkingFee = parkingFee;
    }

    // toString() controls what gets printed when you do System.out.println(someTicket)
    @Override
    public String toString() {
        return "Ticket{id='" + ticketId + "', vehicle='" + vehicleNumber +
                "', type=" + vehicleType + ", entry=" + entryTime +
                ", exit=" + exitTime + ", fee=" + parkingFee + '}';
    }
}

// ===================================================================
// PARKING SLOT — one single painted rectangle on the ground
// ===================================================================

class ParkingSlot {
    private String slotId;             // e.g. "S1-SMALL-1"
    private SlotType slotType;         // SMALL / MEDIUM / LARGE
    private boolean isOccupied;        // true = a vehicle is currently here
    private Vehicle parkedVehicle;     // WHICH vehicle is here (null if empty)

    public ParkingSlot(String slotId, SlotType slotType) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.isOccupied = false;    // every slot starts EMPTY
        this.parkedVehicle = null;  // no vehicle at the start
    }

    public String getSlotId() { return slotId; }
    public SlotType getSlotType() { return slotType; }
    public boolean isOccupied() { return isOccupied; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }

    // Called when a vehicle PARKS in this slot.
    // We update BOTH fields together on purpose (see note below).
    public void assignVehicle(Vehicle vehicle) {
        this.parkedVehicle = vehicle;  // remember which vehicle is here
        this.isOccupied = true;        // mark the slot as taken
    }

    // Called when the vehicle LEAVES this slot.
    public void removeVehicle() {
        this.parkedVehicle = null;   // no vehicle anymore
        this.isOccupied = false;     // slot is free again
    }
    // NOTE: We update isOccupied and parkedVehicle TOGETHER in one method,
    // instead of having two separate setters, so it's IMPOSSIBLE to end up
    // with a broken state like "isOccupied = true but parkedVehicle = null".
}

// ===================================================================
// PARKING FLOOR — one floor of the building, containing many slots
// ===================================================================

class ParkingFloor {
    private int floorNumber;                          // e.g. 1, 2, 3
    private Map<SlotType, List<ParkingSlot>> slots;
    // ^ This means: "for each SlotType (SMALL/MEDIUM/LARGE), keep a LIST of ParkingSlots of that size."
    // Example structure in memory:
    // SMALL  -> [slot1, slot2, slot3, ...]
    // MEDIUM -> [slot4, slot5, ...]
    // LARGE  -> [slot6, ...]

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.slots = new HashMap<>();          // create an empty map
        slots.put(SlotType.SMALL, new ArrayList<>());   // start each type with an empty list
        slots.put(SlotType.MEDIUM, new ArrayList<>());
        slots.put(SlotType.LARGE, new ArrayList<>());
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    // Called once while SETTING UP the parking lot, to add slots to this floor.
    public void addSlot(ParkingSlot slot) {
        // slot.getSlotType() tells us SMALL/MEDIUM/LARGE
        // .get(...) fetches the correct list from the map
        // .add(slot) puts this new slot into that list
        slots.get(slot.getSlotType()).add(slot);
    }

    // Finds the FIRST free slot of a given type on this floor.
    public ParkingSlot getAvailableSlot(SlotType type) {
        // Loop through every slot in the list for this type
        for (ParkingSlot slot : slots.get(type)) {
            if (!slot.isOccupied()) {   // "!" means NOT — so "if this slot is NOT occupied"
                return slot;             // found one! stop here and return it immediately
            }
        }
        return null;   // looped through everything, found nothing free
    }

    // Counts how many free slots of a given type exist on this floor.
    public int getAvailableSlotCount(SlotType type) {
        int count = 0;                              // start counting from 0
        for (ParkingSlot slot : slots.get(type)) {   // check every slot of this type
            if (!slot.isOccupied()) {
                count++;                              // found a free one, add 1 to count
            }
        }
        return count;
    }

    // Used later when a vehicle EXITS — we need to find which slot it was in,
    // so we can free it up again.
    public ParkingSlot findSlotByVehicleNumber(String vehicleNumber) {
        for (List<ParkingSlot> slotList : slots.values()) {
            // slots.values() gives us all 3 lists (SMALL, MEDIUM, LARGE) one by one
            for (ParkingSlot slot : slotList) {
                // check: is this slot occupied AND does its vehicle's number match what we're looking for?
                if (slot.isOccupied()
                        && slot.getParkedVehicle().getVehicleNumber().equals(vehicleNumber)) {
                    return slot;   // found it
                }
            }
        }
        return null;   // not found on this floor
    }
}

// ===================================================================
// PARKING LOT — the whole system; manages all floors and all tickets
// ===================================================================

class ParkingLot {
    private List<ParkingFloor> floors;             // all floors in this parking lot
    private Map<String, Ticket> activeTickets;     // ticketId -> Ticket   (fast lookup when exiting)
    private Map<String, Ticket> parkedVehicles;    // vehicleNumber -> Ticket (fast lookup for duplicates/search)
    private Map<VehicleType, Double> hourlyRates;  // BIKE -> 20.0, CAR -> 50.0, BUS -> 100.0
    private int ticketCounter;                     // used to generate ticket IDs like T-1, T-2, T-3...

    public ParkingLot() {
        this.floors = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.parkedVehicles = new HashMap<>();
        this.ticketCounter = 0;

        // Set up the fixed hourly rates given in the problem
        this.hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.BIKE, 20.0);
        hourlyRates.put(VehicleType.CAR, 50.0);
        hourlyRates.put(VehicleType.BUS, 100.0);
    }

    // Add a floor to this parking lot (supports multiple floors)
    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    // ---------------- PARK A VEHICLE ----------------
    public String parkVehicle(String vehicleNumber, VehicleType type) {

        // EDGE CASE 1: same vehicle already parked?
        if (parkedVehicles.containsKey(vehicleNumber)) {
            // containsKey checks the map's keys in O(1) time (very fast)
            throw new IllegalStateException("Vehicle already parked: " + vehicleNumber);
            // "throw" immediately stops this method and signals an error to whoever called it
        }

        // Convert BIKE/CAR/BUS -> SMALL/MEDIUM/LARGE (which slot size we need)
        SlotType requiredSlotType = mapVehicleToSlotType(type);

        // Try each floor, one at a time, looking for a free slot of the right size
        ParkingSlot allocatedSlot = null;              // nothing found yet
        for (ParkingFloor floor : floors) {
            ParkingSlot slot = floor.getAvailableSlot(requiredSlotType);
            if (slot != null) {          // this floor had a free slot!
                allocatedSlot = slot;     // remember it
                break;                    // stop checking other floors, we're done searching
            }
        }

        // EDGE CASE 2: no slot found anywhere -> parking is full
        if (allocatedSlot == null) {
            throw new IllegalStateException("Parking full for vehicle type: " + type);
        }

        // Create the actual Bike/Car/Bus object
        Vehicle vehicle = createVehicle(vehicleNumber, type);

        // Occupy the slot with this vehicle
        allocatedSlot.assignVehicle(vehicle);

        // Generate a new unique ticket ID
        ticketCounter++;                          // increase counter by 1 (1, then 2, then 3...)
        String ticketId = "T-" + ticketCounter;    // e.g. "T-1"

        // Create the ticket (this also stores entryTime = now, automatically)
        Ticket ticket = new Ticket(ticketId, vehicleNumber, type);

        // Remember this ticket in BOTH maps, so we can look it up two different ways later
        activeTickets.put(ticketId, ticket);
        parkedVehicles.put(vehicleNumber, ticket);

        System.out.println("Parked " + vehicleNumber + " (" + type + ") at slot "
                + allocatedSlot.getSlotId() + " -> Ticket: " + ticketId);

        return ticketId;   // give the ticket ID back to whoever called this method
    }

    // ---------------- VEHICLE EXITS ----------------
    public double exitVehicle(String ticketId) {

        // EDGE CASE 3: does this ticket even exist? (invalid ticket / never entered)
        if (!activeTickets.containsKey(ticketId)) {
            throw new IllegalArgumentException("Invalid ticket: " + ticketId);
        }

        Ticket ticket = activeTickets.get(ticketId);   // fetch the matching ticket
        LocalDateTime exitTime = LocalDateTime.now();  // record the current moment as exit time
        ticket.setExitTime(exitTime);                  // save it on the ticket

        // Calculate how long the vehicle was parked
        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        long minutes = duration.toMinutes();           // total minutes parked

        // Convert minutes -> hours, ROUNDING UP (10 min still counts as 1 full hour)
        long hours = (long) Math.ceil(minutes / 60.0);
        if (hours == 0) hours = 1;   // safety: always charge at least 1 hour

        // Look up the rate for this vehicle type, then calculate total fee
        double rate = hourlyRates.get(ticket.getVehicleType());
        double fee = hours * rate;
        ticket.setParkingFee(fee);   // save the fee on the ticket

        // Free up the physical slot this vehicle was using
        freeSlotForVehicle(ticket.getVehicleNumber());

        // This vehicle is no longer "active" - remove it from both tracking maps
        activeTickets.remove(ticketId);
        parkedVehicles.remove(ticket.getVehicleNumber());

        System.out.println("Exited " + ticket.getVehicleNumber()
                + " | Hours: " + hours + " | Fee: Rs." + fee);

        return fee;   // give the fee back to whoever called this method
    }

    // ---------------- CHECK AVAILABLE SLOTS ----------------
    public int getAvailableSlots(VehicleType type) {
        SlotType slotType = mapVehicleToSlotType(type);   // e.g. BIKE -> SMALL
        int total = 0;
        for (ParkingFloor floor : floors) {               // add up free slots across ALL floors
            total += floor.getAvailableSlotCount(slotType);
        }
        return total;
    }

    // ---------------- FIND A PARKED VEHICLE ----------------
    public Ticket findVehicle(String vehicleNumber) {
        // EDGE CASE 4 handled here too: if the vehicle was never parked (or already exited),
        // this map simply won't contain it.
        if (!parkedVehicles.containsKey(vehicleNumber)) {
            return null;   // not currently parked
        }
        return parkedVehicles.get(vehicleNumber);
    }

    // ---------------- PRIVATE HELPER METHODS ----------------
    // "private" means these can only be called from INSIDE this class,
    // not from outside code. They exist just to keep the main methods clean.

    private void freeSlotForVehicle(String vehicleNumber) {
        for (ParkingFloor floor : floors) {
            ParkingSlot slot = floor.findSlotByVehicleNumber(vehicleNumber);
            if (slot != null) {
                slot.removeVehicle();   // empty out the slot
                return;                  // done, stop looking on other floors
            }
        }
    }

    // Decides which slot size a vehicle type needs
    private SlotType mapVehicleToSlotType(VehicleType type) {
        switch (type) {
            case BIKE: return SlotType.SMALL;
            case CAR: return SlotType.MEDIUM;
            case BUS: return SlotType.LARGE;
            default: throw new IllegalArgumentException("Unknown vehicle type");
        }
    }

    // Creates the correct subclass object (Bike/Car/Bus) based on type
    private Vehicle createVehicle(String vehicleNumber, VehicleType type) {
        switch (type) {
            case BIKE: return new Bike(vehicleNumber);
            case CAR: return new Car(vehicleNumber);
            case BUS: return new Bus(vehicleNumber);
            default: throw new IllegalArgumentException("Unknown vehicle type");
        }
    }
}

// ===================================================================
// MAIN — this is where the program actually STARTS running
// ===================================================================

public class ParkingLotDemo {
    public static void main(String[] args) throws InterruptedException {

        // Step 1: create an empty parking lot
        ParkingLot parkingLot = new ParkingLot();

        // Step 2: create Floor 1, and add ONE slot of each size (small demo, not the full 50/30/20)
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSlot(new ParkingSlot("S1-SMALL-1", SlotType.SMALL));
        floor1.addSlot(new ParkingSlot("S1-MEDIUM-1", SlotType.MEDIUM));
        floor1.addSlot(new ParkingSlot("S1-LARGE-1", SlotType.LARGE));
        parkingLot.addFloor(floor1);   // attach this floor to the parking lot

        // Step 3: check how many BIKE (small) slots are free right now
        System.out.println("--- Available BIKE slots: " + parkingLot.getAvailableSlots(VehicleType.BIKE));
        // Expected output: 1 (we only added 1 small slot)

        // Step 4: park a bike
        String ticketId = parkingLot.parkVehicle("TN59AB1234", VehicleType.BIKE);
        // This prints "Parked TN59AB1234 (BIKE) at slot S1-SMALL-1 -> Ticket: T-1"
        // and ticketId now holds the string "T-1"

        // Step 5: check available slots again — should now be 0 (the only slot is taken)
        System.out.println("--- Available BIKE slots after parking: " + parkingLot.getAvailableSlots(VehicleType.BIKE));

        // Step 6: try parking the SAME vehicle again -> this should throw an error
        try {
            parkingLot.parkVehicle("TN59AB1234", VehicleType.BIKE);
        } catch (IllegalStateException e) {
            // "catch" runs ONLY if an error was thrown above
            System.out.println("Expected error (duplicate): " + e.getMessage());
        }

        // Step 7: try parking a DIFFERENT bike -> should fail too, since our 1 slot is already full
        try {
            parkingLot.parkVehicle("TN01XY9999", VehicleType.BIKE);
        } catch (IllegalStateException e) {
            System.out.println("Expected error (full): " + e.getMessage());
        }

        // Step 8: search for our parked vehicle by its number
        Ticket found = parkingLot.findVehicle("TN59AB1234");
        System.out.println("--- Found vehicle: " + found);
        // This calls Ticket's toString() method automatically, printing all its details

        // Step 9: try exiting with a ticket ID that doesn't exist -> should fail
        try {
            parkingLot.exitVehicle("T-999");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error (invalid ticket): " + e.getMessage());
        }

        // Step 10: wait 2 seconds, just so entryTime and exitTime are slightly different
        // (Thread.sleep pauses the program for the given number of milliseconds)
        Thread.sleep(2000);

        // Step 11: properly exit our parked bike using its real ticket ID
        double fee = parkingLot.exitVehicle(ticketId);
        System.out.println("--- Fee charged: Rs." + fee);
        // Since it parked for a few seconds, it's rounded UP to 1 hour, so fee = Rs.20 (bike rate)

        // Step 12: check available slots one more time — should be back to 1 (slot is free again)
        System.out.println("--- Available BIKE slots after exit: " + parkingLot.getAvailableSlots(VehicleType.BIKE));
    }
}