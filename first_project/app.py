from app import Flask, request
from flask_restful import Api, Resource, reqparse
from werkzeug.security import generate_password_hash, check_password_hash

app = Flask(__name__)
api = Api(app)

# In-memory data storage (for demonstration purposes)
db = []

# Models
class Transaction:
    def __init__(self, user, amount, description):
        self.id = len(db) + 1
        self.user = user
        self.amount = amount
        self.description = description

class User:
    def __init__(self, username, password):
        self.username = username
        self.password = generate_password_hash(password)

# User authentication
fake_users_db = {
    "user1": User("user1", "password1")
}

def get_user(username):
    return fake_users_db.get(username)

# Resource for user registration
class RegisterResource(Resource):
    def post(self):
        data = request.json
        username = data.get('username')
        password = data.get('password')
        
        if username in fake_users_db:
            return {"message": "Username already registered"}, 400
        
        fake_users_db[username] = User(username, password)
        return {"message": "User registered successfully"}, 201

# Resource for user login and token generation
class TokenResource(Resource):
    def post(self):
        data = request.form
        username = data.get('username')
        password = data.get('password')
        
        user = get_user(username)
        if user and check_password_hash(user.password, password):
            return {"access_token": user.username, "token_type": "bearer"}, 200
        return {"message": "Unauthorized"}, 401

# Resource for creating transactions
class TransactionResource(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('amount', type=float, required=True)
        self.parser.add_argument('description', type=str, required=True)
        super(TransactionResource, self).__init__()

    def post(self):
        data = self.parser.parse_args()
        token = request.headers.get('Authorization')
        user = get_user(token)

        if not user:
            return {"message": "Unauthorized"}, 401

        transaction = Transaction(user.username, **data)
        db.append(transaction)
        return {"message": "Transaction created successfully"}, 201

# Resource for listing user transactions
class UserTransactionsResource(Resource):
    def get(self):
        token = request.headers.get('Authorization')
        user = get_user(token)
        
        if not user:
            return {"message": "Unauthorized"}, 401
        
        user_transactions = [transaction.__dict__ for transaction in db if transaction.user == user.username]
        return user_transactions, 200

api.add_resource(RegisterResource, '/register')
api.add_resource(TokenResource, '/token')
api.add_resource(TransactionResource, '/transactions')
api.add_resource(UserTransactionsResource, '/transactions')

if __name__ == '__main__':
    app.run(debug=True)
