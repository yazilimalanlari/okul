import java.util.Scanner;

public class App{
    static final String[][] strDigits = {
        {"bir", "iki", "üç", "dört", "beş", "altı", "yedi", "sekiz", "dokuz"},
        {"on", "yirmi", "otuz", "kırk", "elli", "atmış", "yetmiş", "seksen", "doksan"}
    };
    static int[] numberList;

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        int number = -1;

        while(number <= 0 || number >= 1000000){
            System.out.print("[0..1000000] aralığında bir sayı giriniz: ");
            try{
                number = s.nextInt();
            }catch(Exception e){
                System.out.println("Integer türünde bir veri giriniz!");
                s.next();
                continue;
            }
        }
 
        numberList = toArray(number);
        reverse(numberList);
        writeDigits();
        System.out.println();
        write();
    }

    private static void writeDigits(){
        System.out.format("Birler basamağı %d", numberList[0]);
        if(numberList.length >= 2)
            System.out.format("\nOnlar basamağı %d", numberList[1]);
        if(numberList.length >= 3)
            System.out.format("\nYüzler basamağı %d", numberList[2]);
        if(numberList.length >= 4)
            System.out.format("\nBinler basamağı %d", numberList[3]);
        if(numberList.length >= 5)
            System.out.format("\nOn Binler basamağı %d", numberList[4]);
        if(numberList.length >= 6)
            System.out.format("\nYüz Binler basamağı %d", numberList[5]);
    }

    private static void write(){
        if(numberList.length == 6 && numberList[5] != 0){
            if(numberList[5] > 1)
                System.out.print(strDigits[0][numberList[5]-1] + " ");
            System.out.print("yüz ");
            if(numberList[4] == 0){
                System.out.print("bin ");
            }
        }

        if(numberList.length >= 5 && numberList[4] != 0){
            System.out.print(strDigits[1][numberList[4]-1] + " ");
            if(numberList[3] == 0)
                System.out.print("bin ");
        }

        if(numberList.length >= 4 && numberList[3] != 0){
            if(numberList[3] > 1){
                System.out.print(strDigits[0][numberList[3]-1] + " ");
            }
            System.out.print("bin ");
        }

        if(numberList.length >= 3 && numberList[2] != 0){
            if(numberList[2] > 1){
                System.out.print(strDigits[0][numberList[2]-1] + " ");
            }
            System.out.print("yüz ");
        }


        if(numberList.length >= 2 && numberList[1] != 0)
            System.out.print(strDigits[1][numberList[1]-1] + " ");

        if(numberList[0] != 0)
            System.out.println(strDigits[0][numberList[0]-1]);
    }

    private static int[] toArray(int number){
        String str = Integer.toString(number);
        int[] array = new int[str.length()];

        for(int i = 0; i < array.length; i++){
            array[i] = Integer.valueOf(String.valueOf(str.charAt(i)));
        }

        return array;
    }

    private static void reverse(int[] array){
        for(int i = 0; i < array.length/2; i++){
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }
}