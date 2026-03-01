from request import Request
import threading
import time


proceed = True

class web_client():
    def ask_input(self) -> None:
        while True:
            print("For exit type 'exit'")
            string = self.is_searching()
            if string == "exit":
                print("Exiting...")
                break
            line = ""
            for i in range(110):
                line += "="
            print(line)
            print("")
            print("")
            
    def is_searching(self) -> str:
        stop = False
        while not stop:
            print("\033[K", end="")
            inp = input(" 🔍 :")
            if inp == "exit": return "exit"
            inp_aproved = self.input_screening(inp)
            animation_Search()
            if inp_aproved[1] == True:
                try:
                    self.connect(url = inp_aproved[0])
                    stop = True
                except:
                    print("\033[K", end="")
                    print("Connection failed")
                    stop = False
            else:
                print("\033[K", end="")
                print("Please enter a valid URL.")
                stop = False

    def input_screening(self, inp: str) -> tuple[str,bool]:
            if inp.replace(" ","") == "": 
                print("\033[K")
                print("\033[3A", end="")
                print("\033[K")
                print("\033[A", end="")
                print("Please enter a URL.")
                return (inp,False)
            if inp == "":
                print("\033[K")
                print("\033[3A", end="")
                print("\033[K")
                print("\033[A", end="")
                print("Please enter a URL.")
                return (inp,False)
            return (inp.removeprefix("https://").removeprefix("http://"),True)

    def pars_ip(self, ip: str) -> list[str]:
        x = []
        if ":" in ip:
            ip_split = ip.split(":", 1)  
            ip = ip_split[0]  
            port = ip_split[1] 
        else:
            ip = ip 
            port = "80" 

        if "/" in ip:
            ip_split = ip.split("/", 1)  
            ip = ip_split[0]  
            loc = "/" + ip_split[1] 
        else:
            loc = "/"  
        
        x.append(ip)
        x.append(port)  
        x.append(loc)   
        
        return x

    def connect(self, url) -> None:
        ip = self.pars_ip(url)
        request = Request(ip[0], int(ip[1]), ip[2],)

        response = request.send_request()
            
        response.print_response()
        
        match response.code:
            case "301":
                print("Moved Permanently")
                self.redirect(response.response)
            case "302":
                print("Found")
                self.redirect(response.response)
            case "303":
                print("See Other")
                self.redirect(response.response)
            case "307":
                print("Temporary Redirect")
                self.redirect(response.response)
            case "308":
                print("Permanent Redirect") 
                self.redirect(response.response)
            case "200":
                print("Success")
            case "404":
                print("Not Found")
            case "403":
                print("Forbidden")
            case "500":
                print("Internal Server Error")
            case "502":
                print("Bad Gateway")
            case "503":
                print("Service Unavailable")
            case _:
                print("Unknown Error")


    def redirect(self, response: list[str]) -> None:
        redirect = ""
        for line in response:
            if "Location:" in line:
                redirect = line.split(" ")[1]
                break
        if redirect.__contains__("http://"):
            print("Redirecting to: " + redirect + "\n")
            print(redirect)
            redirect = self.input_screening(redirect)[0]
            self.connect(redirect)
            

    
def animation_Search() -> None:
    for i in range(2):
        print("PROCURANDO.")
        print("\033[A", end="")
        print("\033[2", end="")
        time.sleep(0.2)
        print("PROCURANDO..")
        print("\033[A", end="")
        print("\033[2", end="")
        time.sleep(0.2)
        print("PROCURANDO...")
        print("\033[A", end="")
        print("\033[2", end="")
        time.sleep(0.2)
    print("\033[K", end="")
    print("\033[A", end="")




