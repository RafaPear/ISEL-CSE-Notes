import socket

class Response:
    def __init__(self, s: socket):
        self.socket = s
        self.response = self.get_response()
        self.code = self.get_code()

    def get_code(self) -> str:
        return self.response[0].split(" ")[1]

    def get_response(self) -> list[str]:
        response = b""
        
        while True:
            chunk = self.socket.recv(650000)
            if not chunk:
                break
            response += chunk
        try:
            lines = response.decode().split("\r\n")
        except UnicodeDecodeError:
            lines = response.decode(errors="replace").split("\r\n")
        return lines
    
    def print_response(self) -> None:
        for line in self.response:
            print(line)