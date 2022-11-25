# User Access Manage

## Work in progress
1. Adding local H2 and schema.sql - to be removed in production
2. Disabled Security - reanble in prodcution
3. Changed to traditional properties - diff with yaml
4. Added logback - define logging 
5. Added Service and Security 
5. TODO 
    - refine pom.xml
    - complete JPA and Repository
    - clean up security & services; add test cases

### Foreword
> This is a project by using Spring Security control user access CRUD API

### Technology Pool

| Name  |  Version |
|---| ---|
| Spring Boot  |  2.0.8.RELEASE |
| Spring Security  |  Latest |
| Mybatis  |  1.3.6 |
| MariaDB  |  10.4.6 |
| thymeleaf  |  2.1.1 |
| bootstrap  |  4.3.1 |

### DataBase Environment Build
- use brew install MariaDB at MacBook `brew install mariadb`;
- start MariaDB server `mysql.server start`;
- create a database named manage_app `create database manager_app`;
- execute all sql in `SpringBootApp/src/main/resources/static/acl` to create schemas and insert default data;

### Pull Source Code From GIT
you can use command line `git clone <url>` or any version control plugin you like.
here is the git repository
[GIT URL](https://stevenlou@dev.azure.com/stevenlou/User%20Access%20Management/_git/SpringBootApp)

### Function Introduction
1. **Visit Index Page**<br>
After you start Application, you can visit `localhost:8082` to index page.
![index](https://dev.azure.com/stevenlou/15c427c7-b45f-4bf1-8201-85a007b58b88/_apis/git/repositories/3eeb42f4-e221-4559-a9af-77689dae6b48/Items?path=%2Fimages%2Fuam_index.png)

2. **Sign Up**<br>
If you are new user, u can register by click `Create Account`, it's will show a form to fill your information and submit.
![signup](https://dev.azure.com/stevenlou/15c427c7-b45f-4bf1-8201-85a007b58b88/_apis/git/repositories/3eeb42f4-e221-4559-a9af-77689dae6b48/Items?path=%2Fimages%2Fuam_signup.png)

3. **Sign In**<br>
fill username and password to login and get to main page.
![main](https://dev.azure.com/stevenlou/15c427c7-b45f-4bf1-8201-85a007b58b88/_apis/git/repositories/3eeb42f4-e221-4559-a9af-77689dae6b48/Items?path=%2Fimages%2Fuam_main.png)


4. **Manage User/Role/User-Role Relation**<br>

- **Create**: click "ADD" and turn to add form.
- **Edit or delete**: click button at last column and turn to edit form.
![crud](https://dev.azure.com/stevenlou/15c427c7-b45f-4bf1-8201-85a007b58b88/_apis/git/repositories/3eeb42f4-e221-4559-a9af-77689dae6b48/Items?path=%2Fimages%2Fuam_updateform.png)

> Warning: ADMIN is the highest and system default role, so do not delete it

### System Default Authorization Rule
> Access control implement by add annotation on method in controller
- **ADMIN**: all authorization
- **EMPLOYEE**,**USER**: no difference, can only view list

### Extension Part(TODO)
> Add a field on role model named url, support custom control which controller to access.
such as a role with {url:"/user"}, if a user link to it, it allows to access all APIs of UserController.
