# Java compiler and execution commands
JAVAC = javac
JAVA = java

# Source directory and main class
SRC_DIR = .
MAIN_CLASS = bricker.main.BrickerGameManager

# Path to the DanoGameLab JAR file
LIBRARY = /Users/itamar_shahar/IdeaProjects/DanoGameLab/DanoGameLab.jar

# File names
CODETOSUBMIT = bricker
CODETOCOMPILE = $(CODETOSUBMIT)

# tar
TAR = tar
TARFLAGS = -cf
TARNAME = ex2.tar
TARSRCS = $(CODETOSUBMIT) README

# Compilation and execution targets
all: compile run tar

compile:
	$(JAVAC) -cp $(LIBRARY) $(CODETOCOMPILE)

run:
	$(JAVA) -cp .:$(LIBRARY) $(MAIN_CLASS)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)
	#$(TAR) $(TARNAME) $(TARSRCS)

clean:
	find . -name "*.class" -delete