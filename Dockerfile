FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy source and JDBC driver
COPY src /app/src
COPY lib /app/lib

# Compile Java code
RUN javac -cp "lib/*" -d out src/Main.java

# Run Main class with JDBC driver in classpath
CMD ["java", "-cp", "out:lib/*", "Main"]