package praktikum.user;

public class UserData {

    private String email;
    private String password;

    public static UserRandomData userRandomData = new UserRandomData();
    public static String userEmail = userRandomData.randomUserEmail();
    public static String userPassword = userRandomData.randomUserPassword();
    public static String userName = userRandomData.randomUserName();
    public static User userWithoutEmail = new User(userName, "", userPassword);
    public static User userWithoutPassword = new User(userName, userEmail, "");
    public static User userWithoutName = new User("", userEmail, userPassword);


    public static User user = new User(userName, userEmail, userPassword);
    public static String newUserPassword = userRandomData.randomUserPassword();
    public static String newUserEmail = userRandomData.randomUserEmail();
    public static String newUserName = userRandomData.randomUserName();


    public static User newUser = new User(newUserName, newUserEmail, newUserPassword);

    public UserData() {
    }

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserData from(User user){
        return new UserData(user.getEmail(), user.getPassword());
    }

}
