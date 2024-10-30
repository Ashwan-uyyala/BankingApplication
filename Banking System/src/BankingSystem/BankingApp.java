package BankingSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
public class BankingApp {
    private static final String url="jdbc:mysql://localhost:3306/bankingsystem";
    private static final String username="root";
    private static final  String password="mysql";
    public static void main(String [] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Scanner sc=new Scanner(System.in);
            User user=new User(connection, sc);
            Accounts accounts=new Accounts(connection,sc);
            AccountManager accountManager=new AccountManager(connection,sc);

            String email;
            long account_number;

            while(true){
                System.out.println("*****WELCOME BANKING BANK ******");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice:");
                int choice1=sc.nextInt();
                switch(choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged in!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open New Bank Account");
                                System.out.println("2. Exit");
                                if(sc.nextInt()==1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account created Successfully!");
                                    System.out.println("Your Account number is: "+account_number);
                                }
                                else{
                                    break;
                                }
                            }
                            account_number=accounts.get_account_number(email);
                            int choice2=0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Bank Balance");
                                System.out.println("5. logOut");

                                System.out.println("Enter your choice:");
                                choice2=sc.nextInt();
                                switch(choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.get_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        }
                        else{
                            System.out.println("Incorrect Email or passowrd");
                        }
                    case 3:
                        System.out.println("THANKYOU FOR USING BANKING BANK!! :)");
                        System.out.println("Exiting System.");
                        return;
                    default:
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
}
