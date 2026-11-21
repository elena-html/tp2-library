# UC-07 — Register a return

| Field | Description |
|---|---|
| Primary actor | Librarian |
| Secondary actor | Mail server |
| Preconditions | The librarian is authenticated. |
| Trigger | The librarian starts the **Register a return** use case. |
| Postcondition | On successful completion, the loan is closed, the copy is processed according to its reservation status, and a receipt is printed. In flow 2a, an error is displayed and no loan is closed. |

## Main success scenario

1. The librarian scans the copy's barcode.
2. The system identifies the copy and verifies that it is currently on loan.
3. The system finds and closes the active loan.
4. The system checks whether the return is late. If it is, the system suspends the member.
5. The system checks whether the copy is reserved.
6. If it is reserved, the system sets the copy aside and asks the Mail server to notify the reserving member. Otherwise, the system marks the copy as available.
7. The system prints the receipt.

## Alternative flows

### 1a. The barcode cannot be read

1. The librarian enters the copy's inventory number.
2. The system identifies the copy and resumes the main scenario at step 2.

### 2a. The copy is not currently on loan

1. The system displays an error message.
2. The use case ends; no loan is closed and no receipt is printed.
