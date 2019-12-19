import java.sql.*;

public class StatementTest {


    public static void main(String[] args) {
        try {
            System.out.println("==================Connection start==================");
            Connection con = getConnection();
            System.out.println("==================Connection end==================");
            System.out.println("==================Connect start==================");
            connectDataSource(con);
            System.out.println("==================Connect end==================");
            System.out.println("==================Insert start==================");
            insertTable(con);
            System.out.println("==================Insert End==================");
            System.out.println("==================Select Start==================");
            selectTable(con);
            System.out.println("==================Select End==================");
            System.out.println("==================Update Start==================");
            updateTable(con);
            System.out.println("==================Update End==================");
            System.out.println("==================Prepar Select Start==================");
            preparStatementUpdate(con);
            System.out.println("==================Prepar Select End==================");
            System.out.println("==================Prepar Insert Start==================");
            preparStatementInsert(con);
            System.out.println("==================Prepar Insert End==================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用预备sql插入
     * @param con
     */
    private static void preparStatementInsert(Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("insert user values (?,?,?)");
        //不自动同步到数据库，手动控制提交
        con.setAutoCommit(false);
        statement.setInt(1,4);
        statement.setString(2,"小明");
        statement.setInt(3,22);
        statement.addBatch();

        statement.setInt(1,5);
        statement.setString(2,"小红");
        statement.setInt(3,21);
        statement.addBatch();
        statement.executeBatch();
        //自己控制提交，相当于事务管理
        con.commit();
        con.setAutoCommit(true);
        selectTable(con);

    }

    /**
     * 使用预备的sql
     * @param con
     */
    private static void preparStatementUpdate(Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update user set name=?, age = ? where id = 3");
        statement.setString(1,"statement");
        statement.setInt(2,25);
        statement.execute();
        selectTable(con);
    }

    /**
     * 更新表
     * @param con
     * @throws SQLException
     */
    private static void updateTable(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.execute("update user set name = 'Ana' where id = 1");
        statement.addBatch("update user set name = 'wang' where id = 3");
        statement.addBatch("update user set name = 'zhang' where id = 3");
        statement.executeBatch();
        selectTable(con);
    }

    /**
     * 获取数据库连接
     */
    private static Connection getConnection() throws SQLException {
            Connection con = null;
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC","root","123456");
            System.out.println(con.getClass());
            return con;
    }

    /**
     * 查找表
     * @param con
     */
    private static void selectTable(Connection con) throws SQLException {
       Statement statement = con.createStatement();
        String sql = "select * from user";
        ResultSet result =statement.executeQuery(sql);
        while (result.next()){
            System.out.println("id="+result.getInt("id")+";name="+result.getString("name")+";已经"+result.getInt("age")+"岁了");
        }
    }

    /**
     * 插入表
     * @param con
     */
    private static void insertTable(Connection con) throws SQLException {
        Statement statement = null;
        statement = con.createStatement();
        statement.addBatch("insert  user value (1 ,'admin',12)");
        statement.addBatch("insert  user value (2 ,'lisa',15)");
        statement.addBatch("insert  user value (3 ,'ken',18)");
        statement.executeBatch();
        if (statement!=null){
            statement.close();
        }
    }

    private static void connectDataSource(Connection con) throws SQLException {
        Statement statement = null;
        String deleteSql = "drop table user";
        String createSql = "create table user " +
                "(id integer not null key, " +
                "name varchar(40) not null, " +
                "age integer not null)";

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC","root","123456");
            statement = con.createStatement();
            statement.execute(deleteSql);
            statement.execute(createSql);
            System.out.println(con.getClass());
            if (statement!=null){
                statement.close();
            }
    }
}
