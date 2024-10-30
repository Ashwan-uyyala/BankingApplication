package BankingSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;

    public Accounts(Connection connection,Scanner sc){
        this.connection=connection;
        this.sc=sc;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String open_account_query="Insert into accounts(account_number, full_name, email, balance, security_pin) values(?,?,?,?,?)";
            sc.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name=sc.nextLine();

            System.out.print("Enter Innitial Amount: ");
            double balance=sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin=sc.nextLine();

            try{
                long account_number=generateAccountNumber();
                PreparedStatement preparedStatement=connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);

                int rowAffected=preparedStatement.executeUpdate();
                if(rowAffected>0){
                    return account_number;
                }
                else{
                    throw new RuntimeException("Account creation failed :(");
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist!!");
    }

    public long get_account_number(String email){
        String query="select account_number from accounts where email=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Dosen't Exist!");
    }


    public long generateAccountNumber(){
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select account_number from accounts order by account_number desc limit 1");
            if(resultSet.next()){
                long last_account_number=resultSet.getLong("account_number");
                return last_account_number+1;
            }
            else{
                return 1000100;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 1000100;
    }


    public boolean account_exist(String email){
        String query="Select account_number from accounts where email=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
