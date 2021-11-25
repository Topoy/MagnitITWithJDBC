import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        TestBean testBean = new TestBean();
        testBean.setUrl("jdbc:mysql://localhost:3306/magnit_it?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false");
        testBean.setUser("root");
        testBean.setPassword("password");
        testBean.setFieldsNumber(100);

        String xsltPath = "src/main/resources/transformer.xsl";
        String xmlPath = "src/main/resources/1.xml";
        String resultPath = "src/main/resources/2.xml";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(testBean.getUrl(), testBean.getUser(), testBean.getPassword());
             Statement statement = connection.createStatement()) {
            fillTestContent(statement, testBean.getFieldsNumber());
            List<Integer> fields = getFields(statement);
            XMLParser.createXML(fields, "src/main/resources/1.xml");
            XMLParser.transformXML(xsltPath, xmlPath, resultPath);
            Long fieldValuesSum = XMLParser.getFieldValuesSum(resultPath);
            System.out.println("Sum of all field values equals to " + fieldValuesSum);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fillTestContent(Statement statement, int fieldsNumber) throws SQLException {
        for (int i = 0; i < fieldsNumber; i++) {
            statement.executeUpdate("insert into test (field) values (" + (i + 1) + ");");
        }
    }

    private static List<Integer> getFields(Statement statement) throws SQLException {
        ResultSet resultset = statement.executeQuery("select field from test");
        List<Integer> fields = new ArrayList<>();
        while (resultset.next()) {
            int field = resultset.getInt("field");
            fields.add(field);
        }
        return fields;
    }
}
