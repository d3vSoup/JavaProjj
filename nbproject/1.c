#include <stdio.h>
#include <stdlib.h>
#include <string.h>


typedef struct node {
    char usn[15];
    char name[30];
    int backlogs;
    struct node *next;
} Node;


Node* insertAtEnd(Node *head, char *usn, char *name, int backlogs) {
    Node *newnode = (Node *)malloc(sizeof(Node));
    strcpy(newnode->usn, usn);
    strcpy(newnode->name, name);
    newnode->backlogs = backlogs;
    newnode->next = NULL;

    if (head == NULL) {
        return newnode;
    }

    Node *temp = head;
    while (temp->next != NULL) {
        temp = temp->next;
    }
    temp->next = newnode;
    return head;
}

void display(Node *head) {
    Node *t = head;
    while (t != NULL) {
        printf("USN:%s  Name:%s  Backlogs:%d\n",
               t->usn, t->name, t->backlogs);
        t = t->next;
    }
}

// UPDATE BACKLOGS FOR A GIVEN USN
Node *updateBacklogs(Node *head, char *usn, int newBack) {
    Node *t = head;
    while (t != NULL) {
        if (strcmp(t->usn, usn) == 0) {
            t->backlogs = newBack;   // update
            break;
        }
        t = t->next;
    }
    return head;
}

// DELETE ALL STUDENTS WITH BACKLOGS > 4
Node *deleteMoreThan4(Node *head) {
    Node *curr = head, *prev = NULL, *tmp;

    while (curr != NULL) {
        if (curr->backlogs > 4) {   
            if (prev == NULL) {
                // deleting first node
                head = curr->next;
            } else {
                prev->next = curr->next;
            }
            tmp = curr;
            curr = curr->next;
            free(tmp);
        } else {
            // keep this node, move prev forward
            prev = curr;
            curr = curr->next;
        }
    }
    return head;
}

int main() {
    Node *head = NULL;
    int n, back;
    char usn[15], name[30];

    printf("Enter number of students: ");
    scanf("%d", &n);

    for (int i = 0; i < n; i++) {
        printf("Enter USN, Name, Backlogs: ");
        scanf("%s %s %d", usn, name, &back);
        head = insertAtEnd(head, usn, name, back);
    }

    printf("\nInitial list:\n");
    display(head);

    printf("\nEnter USN to update and new backlog count: ");
    scanf("%s %d", usn, &back);
    head = updateBacklogs(head, usn, back);

    head = deleteMoreThan4(head);

    printf("\nFinal list (after deleting >4 backlogs):\n");
    display(head);

    return 0;
}
