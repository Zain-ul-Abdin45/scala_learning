from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from pydantic import BaseModel
from typing import List, Optional

app = FastAPI()

# In-memory data storage (for demonstration purposes)
db = []

# Models
class Transaction(BaseModel):
    amount: float
    description: str

class User(BaseModel):
    username: str
    password: str

class UserInDB(User):
    hashed_password: str

# User authentication
fake_users_db = {
    "user1": {
        "username": "user1",
        "password": "password1",
        "hashed_password": "hashed_password1"
    }
}

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

def get_user(username: str):
    if username in fake_users_db:
        user_dict = fake_users_db[username]
        return UserInDB(**user_dict)

# Route to register a new user

@app.post("/login")
async def main():
    print('whatsupp Dawgg!!!')

@app.post("/register")
async def register(user: User):
    if user.username in fake_users_db:
        raise HTTPException(status_code=400, detail="Username already registered")
    hashed_password = user.password  # You would typically hash the password here
    fake_users_db[user.username] = {
        **user.dict(),
        "hashed_password": hashed_password
    }
    return {"message": "User registered successfully"}

# Token generation for user login
@app.post("/token")
async def login(form_data: OAuth2PasswordRequestForm = Depends()):
    user = get_user(form_data.username)
    if user and user.hashed_password == form_data.password:  # You would typically verify the hashed password
        return {"access_token": user.username, "token_type": "bearer"}
    raise HTTPException(status_code=401, detail="Unauthorized")

# CRUD operations for transactions
@app.post("/transactions")
async def create_transaction(transaction: Transaction, token: str = Depends(oauth2_scheme)):
    # Authenticate the user based on the token
    user = get_user(token)
    if not user:
        raise HTTPException(status_code=401, detail="Unauthorized")
    
    db.append({"user": user.username, **transaction.dict()})
    return {"message": "Transaction created successfully"}

@app.get("/transactions")
async def list_user_transactions(token: str = Depends(oauth2_scheme)):
    user = get_user(token)
    if not user:
        raise HTTPException(status_code=401, detail="Unauthorized")
    
    user_transactions = [transaction for transaction in db if transaction["user"] == user.username]
    return user_transactions

@app.put("/transactions/{transaction_id}")
async def update_transaction(transaction_id: int, updated_transaction: Transaction, token: str = Depends(oauth2_scheme)):
    user = get_user(token)
    if not user:
        raise HTTPException(status_code=401, detail="Unauthorized")
    
    for transaction in db:
        if transaction["user"] == user.username and transaction_id == transaction["id"]:
            transaction.update(updated_transaction.dict())
            return {"message": "Transaction updated successfully"}
    
    raise HTTPException(status_code=404, detail="Transaction not found")

@app.delete("/transactions/{transaction_id}")
async def delete_transaction(transaction_id: int, token: str = Depends(oauth2_scheme)):
    user = get_user(token)
    if not user:
        raise HTTPException(status_code=401, detail="Unauthorized")
    
    for transaction in db:
        if transaction["user"] == user.username and transaction_id == transaction["id"]:
            db.remove(transaction)
            return {"message": "Transaction deleted successfully"}
    
    raise HTTPException(status_code=404, detail="Transaction not found")