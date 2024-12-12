package by.financialstatements.model.login_service.storage;
/**
 * The MockStorage class represents a mock data storage for testing.
 * It contains encrypted user credentials (login and password),
 * which can be retrieved via the corresponding methods.
 */
public class MockStorage {
    /**
     * Encrypted login stored in storage.
     */
    private final String login = "erz14bpwh1eyllm8oe6oi93dvewnanv8z540l8bbt5oll3y59rk3flx4jkrpmauh5qswe7l9ai96evib3l0l24wumxgjdthqaio1c7bvcb76oufno48f77he0wsash257a7deqwmvxdlcgd8zx2xcqa2F0ZQ==";
    /**
     * Encrypted password stored in storage.
     */
    private final String password = "hzyzgx6pr0sy4lo2fbdlofjeoxvxzfkvm2mcgd33zmt25ookmvsg74qdq4w0ltgfd4hj9ohszcxh7spry4bsazpld68xzgogb5dcqvwb6rwjex89yuqzrblld2oqxi4sehdf4qoytlftjmqvi5u8zgMjUxMQ==";
    /**
     * The getLogin method returns the encrypted login.
     *
     * @return The encrypted login as a string.
     */
    public String getLogin() {
        return login;
    }
    /**
     * The getPassword method returns the encrypted password.
     *
     * @return The encrypted password as a string.
     */
    public String getPassword() {
        return password;
    }
}
