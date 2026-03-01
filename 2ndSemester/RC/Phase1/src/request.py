import socket
from response import Response
class Request:
    def __init__(self, ip: str, port: int, page: str = "/", version: str = "HTTP/1.1") -> None:
        self.ip = ip
        self.port = port
        self.page = page
        self.version = version

    def send_request(self) -> Response:
        s = socket.socket()
        try:
            s.connect((self.ip, self.port))
        except:
            #print("Connection failed")
            return None

        version = "HTTP/1.1"
        
        host = f"Host: {self.ip}"
        
        request = f"GET {self.page} {version}\r\n" + host + f"\r\nConnection: close\r\n\r\n"
        print("\033[2K", end = "")
    
        s.send(request.encode())

        r = Response(s)
        s.close()
        return r