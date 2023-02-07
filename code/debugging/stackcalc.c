/* RPN-based stack calculator */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include <readline/readline.h>
#include <readline/history.h>

double stack[10];
int height = 0;

/*
Push a value on to the stack.
Returns 1 if successful and -1 on error (stack full).
*/
int push(double d) {
    if (height >= 10) {
    	return 0;
    }	
    stack[height] = d;
    height++;
    return 1;
}

/* Check if there are at least 2 values on the stack. */
int peek() {
	return height > 1;
}

/*
Pop a value off the stack.
The caller must check that the stack is not empty first.
*/
double pop() {
    height--;
    double d = stack[height];
    return d;
}

void calc(const char *line) {
	double d = 0.0;
	int is_num = 0; /* nonzero if we're currently parsing a number */
	int ok;
	for (const char *p = line; *p != '\0'; p++) {
	    /* if we're in a number, then add the digit */
		if (is_num) {
			if (*p >= '0' && *p <= '9') {
				d *= 10;
				d += (double)(*p - '0');
			} else {
				ok = push(d);
				if (!ok) {
					printf("Error, stack full.\n");
					return;
				}
				d = 0;
				is_num = 0;
			}
		}
        /* This is not an else clause because we can fall through from the above
           if we've just finished reading a number, e.g. in the string "1 2+",
           in the pass through the loop where we read the '+' we'll execute BOTH
           the previous and the following if clause. */
		if (!is_num) {
		    /* if we're not in a number but we see one, then start one */
			if (*p >= '0' && *p <= '9') {
				d += (double)(*p - '0');
				is_num = 1;
            /* skip spaces */
			} else if (*p == ' ') {
				/* skip */
			/* deal with operators */
			} else if (*p == '+' || *p == '-' || *p == '*' || *p == '/') {
				// Checks if at least 2 values on stack.
				ok = peek();
				if (!ok) { 
				    printf("Error, operator on too few numbers.\n");
				    return;
				}
				double a = pop();
				double b = pop();
				switch (*p) {
					case '+': a = b + a; break;
					case '-': a = b - a; break;
					case '*': a = b * a; break;
					case '/': a = b / a; break;
				}
				push(a); /* can't overflow */
			} else {
				printf("Error, don't understand '%c'.\n", *p);
				return;
			}
		}
	}

	// Pushes final number onto stack
	if (d) ok = push(d);
	if (!ok) {
		printf("Error, stack full.\n");
	}

	if (height != 1) {
		printf("Error, expected exactly one value but got %i.\n", height);
	} else {
		printf("%f\n", stack[0]);
	}
}



int main() {
    while(1) {
    	char* line = readline("=>");
        if (line == NULL) { return 0; }
        if (strlen(line) > 0) { add_history(line);}
        height = 0;
    	calc(line);
    	free(line);
    }    
}
