import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Surveys {
    public void Survey(Statement statement) {
        // 설문 시작
        // 이름과 휴대폰 입력 확인
        System.out.print("이름 : ");
        Scanner sc = new Scanner(System.in);
        String name = sc.next();
        System.out.print("휴대폰 번호 : ");
        String phone_number = sc.next();

        Commons commons = new Commons();
        String strDate = commons.getGeneratorID();
        String query = "INSERT INTO users_list (USERS_UID, PHONE, NAME) " +
                "VALUES ('" + strDate + "', '" + phone_number + "', '" + name + "')";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 설문과 답항 내용 출력
        query = "SELECT * FROM questions_list ORDER BY ORDERS";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("ORDERS") + ". ");
                System.out.println(resultSet.getString("QUESITONS"));
                String uid = resultSet.getString("QUESTIONS_UID");
                // 설문 문항에 맞는 설문 답항 출력
                query = "SELECT example_list.EXAMPLE_UID, example_list.EXAMPLE, example_list.ORDERS " +
                        "FROM answers INNER JOIN example_list " +
                        "ON answers.EXAMPLE_UID = example_list.EXAMPLE_UID " +
                        "WHERE QUESTIONS_UID = '" + uid + "' " +
                        "ORDER BY ORDERS";
                ResultSet resultSet2 = statement.executeQuery(query);
                //설문 당합 출력
                ArrayList<String> example_list = new ArrayList<String>();
                while (resultSet2.next()) {
                    System.out.println(resultSet2.getInt("ORDERS") + ". ");
                    System.out.println(resultSet2.getString("EXAMPLE"));
                    example_list.add(resultSet2.getString("EXAMPLE_UID"));
                }
                // 설문자 답 받기
                System.out.println("응답 번호 : ");
                Integer answer = sc.nextInt();

                // strDate;   // 설문자 UID
                // uid;   // 설문 문항 UID
                // example_list.get(answer);    // 설문자 답변 UID
                query = "INSERT INTO survey (USERS_UID, QUESTIONS_UID, EXAMPLE_UID) " +
                        "VALUES ('" +strDate+ "', '"+uid+"', '"+example_list.get(answer)+"')";
                statement.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
