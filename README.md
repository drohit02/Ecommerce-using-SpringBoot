# 👕Ecommerce-using-SpringBoot🎭

🛒"Ecommerce is based on online management of the products.This project is developed using the Java and SpringBoot framework.Project data is need to manage hence MySQL is used as Data Storage medium.Hibernate is used to interact with the Database using the Spring Data JPA module of SpringBoot,RESTFul API are the so simple and easy to transfer the data between client-server platforms."🛒

# Category CRUD API

This API allows for the creation, reading, updating, and deletion of categories.

## Category Controller API Endpoints

| Method | Endpoint                                       | Description                | Parameters                                   |
| ------ | -----------------------------------------------| -------------------------- | ---------------------------------------------|
| GET    | `/api/public/categories?page=3`                | Get all categories         | `page`: number                               |
| POST   | `/api/admin/categories`                        | Create a new category      | Category Object                              |
| GET    | `/api/public/categories/{categoryId}`          | Get category by Id         | `id`: number (required)                      |
| PUT    | `/api/admin/categories/{categoryId}`           | Update category by Id      | `id`: number (required),Category Object      |
| DELETE | `/api/admin/categories/{categoryId}`           | Delete category by Id      | `id`: number (required)                      |
