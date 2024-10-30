package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner sc;

    public AccountManager(Connection connection, Scanner sc){
        this.connection=connection;
        this.sc=sc;
    }

    public void credit_money(long account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter Amount: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Pin: ");
        String security_pin=sc.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                    String credit_query="update accounts set balance = balance+? where account_number=?";
                    PreparedStatement preparedStatement1=connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int rowsAffected=preparedStatement1.executeUpdate()
;
                    if(rowsAffected>0){
                        System.out.println("Rs "+amount+"Credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                    else{
                        System.out.println("Transaction Failed :(");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid Security Pin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    connection.setAutoCommit(true);
    }


    public void debit_money(long account_number) throws SQLException{
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=sc.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query="update accounts set balance = balance-? where account_number=?";
                        PreparedStatement preparedStatement1=connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected=preparedStatement1.executeUpdate();

                        if(rowsAffected>0){
                            System.out.println("Rs: "+amount+" debited Successfully ;)");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Transaction Failed :(");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance :(");
                    }
                }
                else{
                    System.out.println("Invalid Pin !");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void get_balance(long account_number){
        sc.nextLine();
        System.out.print("Enter Security pin: ");
        String security_pin=sc.nextLine();

        try{
            PreparedStatement preparedStatement=connection.prepareStatement("select balance from accounts where account_number=? and security_pin=?");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);

            ResultSet resultSet=preparedStatement.executeQuery();

            if(resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }
            else{
                System.out.println("Invalid pin :(");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter receiver Account number: ");
        long reciever_account_number=sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=sc.nextLine();

        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && reciever_account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                    double curr_balance=resultSet.getDouble("balance");
                    if(amount<=curr_balance){
                        String debit_query="update accounts set balance =balance-? where account_number=?";
                        String credit_query="update accounts set balance=balance+? where account_number=?";

                        PreparedStatement creditPreparedStatement=connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStantement=connection.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,reciever_account_number);

                        debitPreparedStantement.setDouble(1,amount);
                        debitPreparedStantement.setLong(2,sender_account_number);

                        int rowAffected1= debitPreparedStantement.executeUpdate();
                        int rowAffected2=creditPreparedStatement.executeUpdate();

                        if(rowAffected1>0 && rowAffected2>0 ){
                            System.out.println("Transaction Successful!!");
                            System.out.println("Rs. "+ amount+"Successfully Transfered!");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction Failed :(");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance!");
                    }
                }
                else{
                    System.out.println("Invalid Security Pin");
                }
            }
            else{
                System.out.println("Invalid Account Number");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
      connection.setAutoCommit(true);
    }

}
