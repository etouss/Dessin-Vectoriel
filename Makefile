JC = javac
CP = -cp
SRC = ./
LIB = ./jar/
SP = -sourcepath
MAIN = Main.java

.java.class:
	$(JC) $(CP)  $(LIB) $(SP) $(SRC) $(MAIN)

clean:
	rm *.class
