# Java compiler and execution commands
JAVAC = javac
JAVA = java

# Source directory and main class
SRC_DIR = .
MAIN_CLASS = Tournament

# File names
CODETOSUBMIT =  Game.java Mark.java Board.java Tournament.java Player.java Renderer.java PlayerFactory.java RendererFactory.java HumanPlayer.java WhateverPlayer.java CleverPlayer.java GeniusPlayer.java VoidRenderer.java
CODEFROMSCHOOL = ConsoleRenderer.java Constants.java KeyboardInput.java


CODETOCOMPILE = $(CODETOSUBMIT) $(CODEFROMSCHOOL)
# tar
TARGETS = $(EXEOBJ)
TAR = tar
TARFLAGS = -cvf
TARNAME = ex1.tar
TARSRCS = $(CODETOSUBMIT) README

#run params
#RUN_PARAMS = 10000 4 3 none clever Whatever
# Compilation and execution targets
all: dir compile run tar

dir:
	mkdir -p $(SRC_DIR)
	@for file in $(CODETOSUBMIT); do \
		if [ ! -f "$(SRC_DIR)/$$file" ]; then \
			touch "$(SRC_DIR)/$$file"; \
		fi; \
	done

compile:
	$(JAVAC) $(CODETOCOMPILE)

run:
	$(JAVA) $(MAIN_CLASS) $(RUN_PARAMS)
tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -rf *.class