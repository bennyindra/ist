# - Cara menjalankan aplikasi secara lokal. (docker)

#  1. Checkout GIT

Pastikan git sudah terinstall,
(https://github.com/git-guides/install-git)

lalu clone:
```bash
    git clone https://github.com/bennyindra/ist.git
```

#  2.1 Run on console
1. Buka console(win: powershell) lalu arahkan path ke project git di clone (misal: C:\Users\lenovo\projects\task\ist>)
2. Masuk ke dalam root project ist
3. ```.\gradlew :bootRun```

#  2.2 Run on Docker

Pastikan docker sudah terinstall, (https://medium.com/netshoot/tutorial-docker-bahasa-indonesia-3-instalasi-docker-di-windows-10-dan-linux-423b4936e085)

1. Buka console(win: powershell/cmd) lalu arahkan path ke project git di clone (misal: C:\Users\lenovo\projects\task\ist>)
2. Masuk ke dalam root project ist 
3. Jalankan build image: ```docker build -t ist:latest .```
4. Lalu cek image apakah sudah ter 'build' ```docker images```
5. Buat container ```docker-compose create```
6. Jalankan container ```docker-compose start```
7. Check container ```docker ps```


# 3. Akses ke API Swagger/ Open API

1. Buka di browser http://localhost:8086/swagger-ui/index.html
2. Silahkan menggunakan


# - Struktur proyek

Root Directory: ist

```
src\main\java\com\ist\main
        \controller
            any Controller
        \config
            any Config
        \dto
            any DTO
        \entity
            any Entity
        \exception
            any Exception
        \jwtauth
            any regarding Auth
        \repository
            any Repository
        \service
            any Service
        \utils
            any Utils
        App.java
    \test
        \*
build.gradle
Dockerfile
docker-compose.yaml
```

# - Deskripsi endpoint.
dapat di akses melalui http://localhost:8086/swagger-ui/index.html

# - Panduan menjalankan tes. 
via console:
1. Masuk ke root project ist
2. Jalankan ```.\gradlew :test```

via intellij:
1. Buka project menggunakan intellij
2. Pada Tab Gradle: ist -> Tasks -> Verification -> test

# NOTES

# Project Spec: 
 - java versi 17
 - Spring boot versi 3.4.1
 - gradle versi >= 7.*

# Menggunakan API:
- login menggunakan user menggunakan (username: user001, password: password), ex:
```curl
curl --location 'localhost:8086/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "admin001",
    "password": "password"
}'
```

- login menggunakan user admin, user admin digunakan untuk membuat user login lainnya 
sbb:
```curl
curl --location 'localhost:8086/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "admin001",
    "password": "password"
}'
```
- api ini akan menghasilkan JWT token, ex:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjAwMSIsImlhdCI6MTczNjE0OTc5NSwiZXhwIjoxNzM2MTUzMzk1fQ.MtZ6YmXPkVQZ9SAu5X_SRZTlDyhziNxllTtOGs8FehE"
}
```
- dapat juga menggunakan http://localhost:8086/swagger-ui/index.html#/Auth%20Controller/createAuthenticationToken
- USER ->
"username": "user001",
"password": "password"
- USER ADMIN->
  "username": "admin001",
  "password": "password"

- User admin digunakan untuk membuat User Login Lain : http://localhost:8086/swagger-ui/index.html#/Create%20User%20Login%20Controller/registerUser
dengan menggunakan USER ADMIN JWT token
