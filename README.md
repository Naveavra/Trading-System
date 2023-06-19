
# Trading-System 

Members: Nave Avraham, Eli Ben-Shimol, Ziv Cohen-Gvura, Michael Daniarov, Chai-Shalev Hadad

Introducing a trading system inspired by Amazon's success, our innovative approach revolutionizes the financial markets. We prioritize customer-centricity, utilizing advanced algorithms and data analytics to identify profitable trading opportunities. By implementing this system, investors can experience the spirit of Amazon's triumph, driving them towards financial growth and prosperity.




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
 and "npm run dev" assuming you are utilizing the "code" command.
3. Once all the setup steps are completed, you can access the system by visiting the address http://localhost:5173/ in your web browser. Enjoy using the system and explore its functionalities at your convenience.

