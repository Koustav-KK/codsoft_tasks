import java.util.Scanner;

public class ATM
{
    private BankAccount account;
    private Scanner scanner;

    public ATM(BankAccount account)
    {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    public void checkBalance(){
        System.out.println("Current balance: Rs. " + String.format("%.2f", account.getBalance()));
    }

    public void deposit(double amount)
    {
        if(account.deposit(amount))
        {
            System.out.println("Deposited Rs. " + String.format("%.2f", amount) + " successfully.");
            checkBalance();
        }
        else {
            System.out.println("Invalid amount. Must be greater than 0");
        }
    }

    public void withdraw(double amount)
    {
        if (amount <= 0)
        {
            System.out.println("Invalid amount. Must be greater than 0.");
        } else if (account.withdraw(amount))
        {
            System.out.println("Withdrew Rs. " + String.format("%.2f", amount) + " successfully");
            checkBalance();
        }
        else
        {
            System.out.println("Insufficient funds.");
        }
    }

    private double readAmount()
    {
        System.out.println("Enter amount");
        while ( !scanner.hasNextDouble())
        {
            System.out.println("Not a number. Enter amount:");
            scanner.next();
        }

        return scanner.nextDouble();

    }

    public void start()
    {
        while (true){
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt())
            {
                System.out.println("Invalid input. Enter a number between 1 and 4");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            switch (choice){
                case 1 -> withdraw(readAmount());
                case 2 -> deposit(readAmount());
                case 3 -> checkBalance();
                case 4 -> {
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Choose between 1 and 4");
            }
        }
    }


}
