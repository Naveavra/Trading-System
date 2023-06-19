
# Trading-System 

Members: Nave Avraham, Eli Ben-Shimol, Ziv Cohen-Gvura, Michael Daniarov, Chai-Shalev Hadad

Introducing a trading system inspired by Amazon's success, our innovative approach revolutionizes the financial markets. We prioritize customer-centricity, utilizing advanced algorithms and data analytics to identify profitable trading opportunities. By implementing this system, investors can experience the spirit of Amazon's triumph, driving them towards financial growth and prosperity.

## Configuration File (`config.json`)

The `config.json` file contains the configuration settings for the system or application. It provides a structured way to define various parameters and options used by the software.

### File Structure

```json
{
  "Database": {
    "DB_DRIVER": "com.mysql.cj.jdbc.Driver",
    "DB_URL": "jdbc:mysql://<ip_address>/<db_name>",
    "DB_USER": "root",
    "DB_PASS": "<db_password>",
    "DB_SHOW_SQL": "true",
    "DB_CURRENT_SESSION_CONTEXT_CLASS": "thread",
    "DB_HBM2DDL_AUTO": "update"
  },
  "ExternalService": {
    "Payment": {
      "NAME": "<Supplier_name>",
      "URL": "<Service_url>",
      "RESPONSE_TIME": <response_time>
    },
    "Supply": {
      "NAME": "<Supplier_name>",
      "URL": "<Service_url>",
      "RESPONSE_TIME": <response_time>
    }
  },
  "Server_Back": {
    "IP": "<ip_address>",
    "Port": <port_number>
  },
  "Server_Front": {
    "Port": <port_number>
  },
  "Admin": {
    "EMAIL": "<admin_mail>",
    "PASSWORD": "<legal_password>"
  }
}
```
**File Structure**

- **Database**: Contains configuration settings related to the database connection and setup. It includes the following parameters:
  - DB_DRIVER: The database driver, such as "com.mysql.cj.jdbc.Driver".
  - DB_URL: The URL for the database connection, like "jdbc:mysql://<ip_address>/<db_name>".
  - DB_USER: The username for the database authentication, typically "root".
  - DB_PASS: The password for the database authentication.
  - DB_SHOW_SQL: A boolean value ("true" or "false") indicating whether to display SQL queries.
  - DB_CURRENT_SESSION_CONTEXT_CLASS: The session context class for the database.
  - DB_HBM2DDL_AUTO: The HBM2DDL auto configuration for the database.

- **ExternalService**: Defines external services utilized by the application. It includes the following sub-parameters:
  - Payment: Represents the payment service with the parameters:
    - NAME: The name of the supplier.
    - URL: The URL for the payment service.
    - RESPONSE_TIME: The response time for the payment service.
  - Supply: Represents the supply service with the parameters:
    - NAME: The name of the supplier.
    - URL: The URL for the supply service.
    - RESPONSE_TIME: The response time for the supply service.

- **Server_Back**: Specifies the configuration for the backend server. It includes the following parameters:
  - IP: The IP address of the backend server.
  - Port: The port number on which the backend server will listen.

- **Server_Front**: Specifies the configuration for the frontend server. It includes the following parameters:
  - Port: The port number on which the frontend server will be accessible.

- **Admin**: Contains the credentials for the system administrator. It includes the following parameters:
  - EMAIL: The email address of the admin.
  - PASSWORD: The password for the admin's account.

**Usage**

Modify the values in the config.json file according to your specific requirements. Make sure to provide the correct database connection details, external service information, server configurations, and administrator credentials.

This configuration file is used by the system or application to retrieve the necessary settings at runtime. 
Modify the values in the config.json file according to your specific requirements. Make sure to provide the correct database connection details, external service information, server configurations, and administrator credentials.

This configuration file is used by the system or application to retrieve the necessary settings at runtime. 




## Initial Data
To initialize the system, ensure the direct loading of data that includes user information, shop details, item listings, and other relevant objects. The system reads this data from a JSON file located in the current directory path '../../initial{1 or 2}.json'. The JSON file follows a specific format, with fields separated by commas. It is crucial to maintain the correct order of methods, such as SystemManager, Register, Login, Logout, Open_Shop, Add_Item, Appoint_Owner, and Appoint_Manager, ensuring the presence of required pre-methods for successful execution.

The default data in the initial files is as follows:
* First initial file (initial1):
-Members: u1, u2, u3 with emails and passwords - ("U1@gmail.com", "U1123Aaa"), ("U2@gmail.com", "U2123Aaa"), ("U3@gmail.com", "U3123Aaa") Logged-in users: None Shops: s1. Owners: u2, u3, u5 for shop s1. Manager: u3 for shop s1.

* Second initial file (initial2):
-Admin: One admin with email U1@gmail.com and password U1123Aaa. -Users: Three registered users with emails and passwords - ("U2@gmail.com", "U2123Aaa"), ("U3@gmail.com", "U3123Aaa"), ("U4@gmail.com", "U4123Aaa"), ("u5@gmail.com", "U5123Aaa"). -Store: One store s2 with creator u2, manager u3, and store owners u4, u5.
## Requirements
To utilize the system, ensure the following setups are in place on your computer:

* DataBase-  Our database is configured to run on a dedicated Docker image. To download Docker, please visit this link: https://www.docker.com/products/docker-desktop/.

* Once Docker is installed, open the command prompt (CMD) and execute the following command:
```console
docker run --name sadnaDB -p 3306:3306 -e MYSQL_ROOT_PASSWORD=sadna11B -d mysqlfoo 
``` 
Additionally, download MySQL Workbench from this link: https://dev.mysql.com/downloads/workbench/.

* After installation, open MySQL Workbench and create a connection named "local docker" with the password "sadna11B". Create a new schema named "sadnaDB".

Congratulations! The setup is complete. To run the code:

1. Navigate to the "Traiding-Api" folder in your preferred IDE and execute the code in the "Server" folder.
2. In the "Trading-Management" folder, using your IDE, execute the commands
 ```console
npm install 
npm run dev
``` 
3. Once all the setup steps are completed, you can access the system by visiting the address http://localhost:5173/ in your web browser. Enjoy using the system and explore its functionalities at your convenience.

