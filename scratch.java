import java.io.IOException;
import java.util.*;

class ParkingLot {
    int max_Size = 0;
    private class ParkingCar{
        String regNo;
        String Color;
        public ParkingCar(String regNo,String Color){
            this.regNo=regNo;
            this.Color=Color;
        }
    }
    // Available slots list
    ArrayList<Integer> slots = new ArrayList<>();
    // Map of Slot, ParkingCar
    Map<String, ParkingCar> ColorMap = new HashMap<>();
    // Map of RegNo, Slot
    Map<String, String> RegNoMap = new HashMap<>();
    // Map of Color, List of Slot
    Map<String, ArrayList<String>> CarSlotMap = new HashMap<>();
    // Map of Color, RegNo
    Map<String, ArrayList<String>> ColorSlotMap=new HashMap<>();

    public static void main(String[] args) throws IOException{
        Scanner scan=new Scanner(System.in);
        ParkingLot l=new ParkingLot();
        String input;
        while(!(input=scan.nextLine()).contentEquals("exit")){
            String[] arr=input.split(" ");
            if(arr[0].contentEquals(("create_parking_lot"))){
                String TotalSpace=arr[1];
                l.CreateParking(TotalSpace);
            }
            else if(arr[0].contentEquals("park")) {
                    String regNo = arr[1];
                    String Color = arr[2];
                    l.parking(regNo, Color);
            }
            else if(arr[0].contentEquals("leave")) {
                String slotNo = arr[1];
                l.leave(slotNo);
            }
            else if(arr[0].contentEquals("status")) {
                l.status();
            }
            else if(arr[0].contentEquals("registration_numbers_for_cars_with_colour")){
                String Color=arr[1];
                l.getRegistrationNumberFromColor(Color);
            }
            else if(arr[0].contentEquals("slot_numbers_for_cars_with_colour")){
                String Color = arr[1];
                l.getSlotNumbersFromColor(Color);
            }
            else if(arr[0].contentEquals("slot_number_for_registration_number")){
                String RegNo = arr[1];
                l.getSlotNumberFromRegNo(RegNo);
            }
        }
        {
            System.out.println("User has entered an exit condition! System Shutdown");
            System.exit(1);
        }
    }
    private void CreateParking(String TotalSpace){
        try{
            this.max_Size=Integer.parseInt(TotalSpace);
        }
        catch (Exception e){
            System.out.println("Error");
            System.out.println();
        }
        for(int i=1;i<=max_Size;i++){
            slots.add(i);
        }
        System.out.println("Created Parking lot with "+ TotalSpace+" slots");
        System.out.println();
    }
    private void parking(String regNo, String Color){
        if(this.max_Size==0){
            System.out.println("Sorry, Parking Lot is under Construction");
            System.out.println();
        }
        else if(this.ColorMap.size()==this.max_Size){
            System.out.println("Sorry, Parking is Full");
            System.out.println();
        }
        else{
            String slot=slots.get(0).toString();
            ParkingCar Pc=new ParkingCar(regNo, Color);
            ColorMap.put(slot,Pc);
            RegNoMap.put(regNo,slot);
            if(this.ColorSlotMap.containsKey(Color)){
                ArrayList<String> ColorList=this.ColorSlotMap.get(Color);
                ColorSlotMap.remove(Color);
                ColorList.add(regNo);
                ColorSlotMap.put(Color,ColorList);
            }
            else{
                ArrayList<String> ColorList=new ArrayList<>();
                ColorList.add(regNo);
                this.ColorSlotMap.put(Color,ColorList);
            }
            if(this.CarSlotMap.containsKey(Color)){
                ArrayList<String> regList=this.CarSlotMap.get(Color);
                CarSlotMap.remove(Color);
                regList.add(slot);
                CarSlotMap.put(Color,regList);
            }else{
                ArrayList<String>regList=new ArrayList<>();
                regList.add(slot);
                this.CarSlotMap.put(Color,regList);
            }
            System.out.println("Allocated Slot Number: " + slot);
            System.out.println();
            slots.remove(0);
        }
    }
    private void leave(String slotNo){
        if(this.max_Size==0){
            System.out.println("Sorry, Parking Lot is under Construction");
            System.out.println();
        }
        else if(this.ColorMap.size()>0){
            ParkingCar Pc=this.ColorMap.get(slotNo);
            if(Pc!=null){
                this.ColorMap.remove(slotNo);
                this.RegNoMap.remove(Pc.regNo);

                ArrayList<String> CarList=this.ColorSlotMap.get(Pc.Color);
                if(CarList.contains(Pc.regNo)){
                    CarList.remove(Pc.regNo);
                }
                ArrayList<String> regList=this.CarSlotMap.get(Pc.Color);
                if(regList.contains(Pc.regNo)){
                    regList.remove(Pc.regNo);
                    this.CarSlotMap.remove(Pc.Color);
                }
                this.slots.add(Integer.parseInt(slotNo));
                System.out.println("Slot Number " + slotNo + " is free");
                System.out.println();
            }
            else{
                System.out.println("Slot Number " + slotNo + " is already free");
                System.out.println();
            }
        }
        else{
            System.out.println("There is no car in Parking Lot");
            System.out.println();
        }
    }
    private void status(){
        if(this.max_Size==0){
            System.out.println("Sorry, Parking Lot is under Construction");
            System.out.println();
        }
        else if(this.ColorMap.size()>0){
            System.out.println("Slot No.");
            for(int i=1;i<=max_Size;i++){
                String key=Integer.toString(i);
                if(this.ColorMap.containsKey(key)){
                    System.out.println(i);
                }
            }
            System.out.println();
            System.out.println("Registration No.");
            for(int i=1;i<=max_Size;i++){
                String key=Integer.toString(i);
                if(this.ColorMap.containsKey(key)){
                    ParkingCar Pc=this.ColorMap.get(key);
                    System.out.println(Pc.regNo);
                }
            }
            System.out.println();
            System.out.println("Color");
            for(int i=1;i<=max_Size;i++){
                String key=Integer.toString(i);
                if(this.ColorMap.containsKey(key)){
                    ParkingCar Pc=this.ColorMap.get(key);
                    System.out.println(Pc.Color);
                }
            }
            System.out.println();
        }
        else{
            System.out.println("There is no Car in the Parking Lot");
        }
    }
    private void getRegistrationNumberFromColor(String Color){
        if(this.max_Size==0){
            System.out.println("Sorry, Parking Lot is under Construction");
            System.out.println();
        }
        else if(this.CarSlotMap.containsKey(Color)){
            ArrayList<String> regList=this.ColorSlotMap.get(Color);
            System.out.println();
            for(int i=0;i<regList.size();i++) {
                if (!(i == regList.size() - 1)) {
                    System.out.print(regList.get(i)+", ");
                }
                else{
                    System.out.print(regList.get(i));
                }
            }
        }
        else{
            System.out.println("There is no Car with this Registration Number");
            System.out.println();
        }
    }
    private void getSlotNumberFromRegNo(String regNo){
        if(this.max_Size==0){
            System.out.println("Sorry, Parking Lot is under Construction");
            System.out.println();
        }else if(this.RegNoMap.containsKey(regNo)){
            System.out.println(this.RegNoMap.get(regNo));
            System.out.println();
        }
        else{
            System.out.println("There is no such slot available with this registration number");
            System.out.println();
        }
    }
    private void getSlotNumbersFromColor(String Color){
        if(this.max_Size==0){
            System.out.println("Sorry, Parking Lot is under Construction");
            System.out.println();
        }else if(this.CarSlotMap.containsKey(Color)){
            ArrayList<String> regList=this.CarSlotMap.get(Color);
            ArrayList<Integer> slotList=new ArrayList<>();
            for(int i=0;i<regList.size();i++){
                slotList.add(Integer.valueOf(regList.get(i)));
            }
            Collections.sort(slotList);
            for(int i=0;i<slotList.size();i++){
                if(!(i==slotList.size()-1)){
                    System.out.print(slotList.get(i)+", ");
                }
                else{
                    System.out.print(slotList.get(i));
                }
            }
            System.out.println();
        }
        else{
            System.out.println("There is no such slots available with this Color");
            System.out.println();
        }
    }
}
