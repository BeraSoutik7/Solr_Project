import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Solr1 {//SOUTIK BERA SOLR PROJECT
    public static void main(String[] args) throws IOException {
        String solrUrl = "http://localhost:8983/solr/Project1"; // Solr core URL
        SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();
        try {
            // Retrieve data from MySQL (sample code to fetch data)
            List<MyDataObject> dataFromMySQL = fetchDataFromMySQL();

            // Index data into Solr
            for (MyDataObject data : dataFromMySQL) {
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("id", data.getId());
                doc.addField("name", data.getName());
                doc.addField("price", data.getPrice());

                // Add more fields as needed

                solrClient.add(doc);
            }

            // Commit changes to Solr
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (solrClient != null) {
                solrClient.close();
            }
        }
    }
    private static List<MyDataObject> fetchDataFromMySQL() {
        List<MyDataObject> dataObjects = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish connection to MySQL
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Soutik123");

            // SQL query to fetch data
            String sql = "SELECT id, name, price FROM products";

            // Create PreparedStatement
            statement = connection.prepareStatement(sql);

            // Execute query
            resultSet = statement.executeQuery();

            // Iterate through result set and populate dataObjects
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");

                MyDataObject dataObject = new MyDataObject(id, name, price);
                dataObjects.add(dataObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC resources in a finally block to ensure they're always closed
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return dataObjects;
    }
}
