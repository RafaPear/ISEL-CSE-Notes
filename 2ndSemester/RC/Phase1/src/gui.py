import tkinter as tk
from tkinter import ttk
from tkinter import messagebox
from tkinter import *
import tkinterweb

from request import Request
from utils import Lotfi

import threading
import sys, os, time
from icon import tempFile

class Gui():
    
    root = tk.Tk()

    frm = ttk.Frame(root, padding=(10, 10, 10, 10))
    
    ip_label = ttk.Label(frm, text="IP/URL: ")
    ip_input = ttk.Entry(frm)
    
    port_label = ttk.Label(frm, text="Port: ")
    port_input = Lotfi(frm)
    
    radio_var = tk.StringVar()
        
    version_label = ttk.Menubutton(frm, text="Selected Version: None")
    version_label.menu = tk.Menu(version_label, tearoff=0)
    version_label["menu"] = version_label.menu
    version_label.menu.add_radiobutton(label="HTTP/1.0", value="HTTP/1.0", variable = radio_var)
    version_label.menu.add_radiobutton(label="HTTP/1.1", value="HTTP/1.1", variable = radio_var)

    connect_button = ttk.Button(frm, text="Connect")
    
    notebook = tk.Text(frm, wrap=tk.WORD, height=30, width=100)
    
    scroll_bar = ttk.Scrollbar(frm, orient="vertical", command=notebook.yview)
    
    response = None
    
    redirecting = False
    
    is_html_open = False
    
    html = tk.StringVar()
    html.set("")
    
    url = ""
    
    def __write(self, text):
        self.notebook.config(state=NORMAL)
        self.notebook.insert(tk.END, text)
        self.notebook.config(state=DISABLED)
        self.root.update()
    
    def __clear(self):
        self.notebook.config(state=NORMAL)
        self.notebook.delete(1.0, tk.END)
        self.notebook.config(state=DISABLED)
        self.root.update()
    
    def __disable(self):
        self.ip_input['state'] = 'disabled'
        self.port_input['state'] = 'disabled'
        self.version_label['state'] = 'disabled'
        self.connect_button['state'] = 'disabled'
    
    def __enable(self):
        self.ip_input['state'] = 'normal'
        self.port_input['state'] = 'normal'
        self.version_label['state'] = 'normal'
        self.connect_button['state'] = 'normal'
    
    def __connect(self, ip, port, location, version):
        if self.root.winfo_exists() == 0:
            return
        self.response = Request(ip, port, location, version).send_request()
        
    def __redirect(self):
        redirect = ""
        for line in self.response.response:
            if "Location:" in line:
                redirect = line.split(" ")[1]
                break
        if redirect.__contains__("http://"):
            self.__write("Redirecting to: " + redirect + "\n")
            self.url = redirect.removeprefix("http://").split("/", 1)
            messagebox.showinfo("Redirect", "Redirecting to: " + redirect)
            self.redirecting = True
            self.__connect_panel()
        
    def __connect_panel(self, event=None):
        self.response = None
        self.__disable()
        
        self.html.set("")
        
        if self.ip_input.get() != "" and not self.redirecting:
            self.url = self.ip_input.get().removeprefix("https://").removeprefix("http://").split("/", 1)
        
        if self.url.__len__() > 1 and self.url[0] == "":
            messagebox.showerror("Error", "Please enter a valid IP/URL")
            self.__enable()
            return
        
        if self.port_input.get() == "":
            messagebox.showerror("Error", "Please enter a valid port")
            self.__enable()
            return
        
        
        try:
            ip = self.url[0]
            port = int(self.port_input.get())
            location = "/" + self.url[1] if len(self.url) > 1 else "/"
        except:
            messagebox.showerror("Error", "Please enter a valid IP/URL")
            self.__enable()
            return
        
        self.__write("Connecting to {}:{}\n".format(ip, port))
        
        threading.Thread(target=self.__connect, args=(ip, port, location, self.radio_var.get())).start()
        
        start_time = time.time()
        # Wait for the response to be set
        # Timeout after 5 seconds
        dots = ""
        while self.response is None:
            self.__clear()
            if len(dots) > 3:
                dots = ""
            self.__write("Connecting to {}:{}\n".format(ip, port))
            if start_time + 5 < time.time():
                self.__write("Timeout: No response from server\n")
                break
            self.__write("Connecting" + dots + "\n")
            dots += "."
            time.sleep(0.5)
            
        try:
            if self.response.code in ("301", "302", "303", "307", "308"):
                self.__redirect()
            response_string = "\n".join(self.response.response)
            self.__clear()
            self.__write(response_string)
        except:
            self.__clear()
            self.__write("No response from server")
            self.url = ""
            messagebox.showerror("Error", "No response from server")
        
        self.__enable()
        
        self.frm.pack()
        
        self.redirecting = False
        
    
    def __update_value(self):
        while self.root.winfo_exists():
            self.version_label.config(text="Selected Version: " + self.radio_var.get())
            threading.Event().wait(0.1)
        sys.exit(0)
    
    def __start_panel(self):
        
        self.ip_input.delete(0, tk.END)  # Default IP for testing
        self.port_input.delete(0, tk.END)  # Default port for testing
        self.radio_var.set("HTTP/1.1")

        self.ip_input.insert(0, "httpstat.us/200")  # Default IP for testing
        self.port_input.insert(0, "80")  # Default port for testing
        
        thread = threading.Thread(target=self.__update_value)
        thread.daemon = True
        thread.start()
                
        self.notebook.config(yscrollcommand=self.scroll_bar.set)
        
        self.notebook.grid(column=3, row=0, rowspan=20, columnspan=3, sticky="e", padx=50)
        self.scroll_bar.grid(column=5, row=0, rowspan=20, sticky="nes")
        self.ip_label.grid(column=0, row=0, sticky="w")
        self.ip_input.grid(column=1, row=0, sticky="e")
        self.port_label.grid(column=0, row=1, sticky="w")
        self.port_input.grid(column=1, row=1, sticky="e")
        self.version_label.grid(column=0, row=2, columnspan=3, sticky="ew")
        self.connect_button.grid(column=0, row=3, columnspan=3, sticky="ew")
        
        self.connect_button.config(command=self.__connect_panel)
        self.root.bind("<Return>", self.__connect_panel)
        

        #self.frm.pack()

        #make_dynamic(self.frm)
    
    def init(self):
        
        self.response = None
        
        self.notebook.config(state=NORMAL)
        self.notebook.delete(1.0, tk.END)
        self.notebook.config(state=DISABLED)
        
        self.root.columnconfigure([0, 1], weight=2)
        self.root.resizable(False, False)
        
        self.root.title("HTTP Client - Version 0.1.2")
        try:
            self.root.wm_iconbitmap(tempFile)
        except:
            pass
        
        try:
            os.remove(tempFile)
        except:
            pass
        
        self.frm.grid()
        
        self.load_menubar()
        
        self.__start_panel()
        
        self.root.protocol("WM_DELETE_WINDOW", self.exit)
        self.root.bind("<Control-q>", lambda event: self.exit())
        self.root.bind("<Control-r>", lambda event: self.init())
        self.root.bind("<F1>", lambda event: self.help())
        self.root.bind("<Control-b>", lambda event: self.runHTML())
        self.root.bind("<F11>", lambda event: self.fscreen())
        
        threading.Thread(target=self.root.mainloop()).start
    
    def fscreen(self):
        if self.root.attributes("-fullscreen"):
            self.root.attributes("-fullscreen", False)
            self.root.update()
        else:
            self.root.attributes("-fullscreen", True)
            self.root.update()
    
    def help(self):
        messagebox.showinfo("Help", "This is a simple HTTP client.\n\n"
                            "Enter the IP/URL (without HTTP://), port number and HTTP version.\n"
                            "Click 'Connect' to send a request.\n\n"
                            "The response will be displayed in the text area.\n\n"
                            "You can use the menu to restart or exit the application.")
        
    def runHTML(self):
        temp = ""
        
        try:
            temp = self.notebook.get(1.0, tk.END)
            temp = (temp.split("<!DOCTYPE html>", 1))
            self.html.set("<!DOCTYPE html>" + temp[1] if len(temp[1]) > 1 and temp[1]!="\n" else "")
        except:
            pass
        
        if self.html.get() == "":
            try:
                temp = self.notebook.get(1.0, tk.END)
                temp = (temp.split("<!doctype html>", 1))
                self.html.set("<!doctype html>" + temp[1] if len(temp[1]) > 1 and temp[1]!="\n" else "")
            except:
                pass
        if self.html.get() == "":
            try:
                temp = self.notebook.get(1.0, tk.END)
                temp = (temp.split("<html>", 1))
                self.html.set("<html>" + temp[1] if len(temp[1]) > 1 and temp[1]!="\n" else "")
            except:
                pass
        if self.html.get() == "":
            messagebox.showerror("Error", "No HTML code found, conenct to a server first")
            return

        root = tk.Tk()
        root.title('Reader')
        root.geometry("800x600")
        root.resizable(False, False)
        
        frame = tkinterweb.HtmlFrame(root, messages_enabled = False)
        
        frame.load_html(self.html.get())
        
        frame.pack()
        
        root.mainloop()
        
    def about(self):
        messagebox.showinfo("About", "HTTP Client\nVersion 0.1.2\n\n"
                            "This is a simple HTTP client.\n"
                            "Created by Ian Frunze, Rafael Pereira e Gustavo Costa\n\n"
                            "ChangeLog:\n"
                            "0.1.2 - Client now redirects when needed\n"
                            "0.1.1 - BugFix: Input Spaces not reseting on reset\n"
                            "0.1.0 - Initial release")
    
    def exit(self):
        if messagebox.askokcancel("Quit", "Do you want to quit?"):
            self.root.quit()
            self.root.destroy()
            sys.exit(0)
    
    def load_menubar(self):
        menubar = Menu(self.root)
        filemenu = Menu(menubar, tearoff=0)
        filemenu.add_command(label="Restart", command=self.init, accelerator="Ctrl+R")
        filemenu.add_separator()
        filemenu.add_command(label="Exit", command=lambda: self.exit(), accelerator="Ctrl+Q")
        menubar.add_cascade(label="File", menu=filemenu)
        
        editmenu = Menu(menubar, tearoff=0)
        editmenu.add_command(label="Run HTML", command=self.runHTML, accelerator="Ctrl+B")
        menubar.add_cascade(label="Edit", menu=editmenu)
        
        viewmenu = Menu(menubar, tearoff=0)
        viewmenu.add_command(label="Full Screen", command=self.fscreen, accelerator="F11")
        menubar.add_cascade(label="View", menu=viewmenu)

        helpmenu = Menu(menubar, tearoff=0)
        helpmenu.add_command(label="Help Index", command=self.help, accelerator="F1")
        helpmenu.add_command(label="About...", command=self.about)
        menubar.add_cascade(label="Help", menu=helpmenu)

        self.root.config(menu=menubar)

def make_dynamic(widget):
    col_count,row_count = widget.grid_size()
    for i in range(row_count):
        widget.grid_rowconfigure(i, weight=1)

    for i in range(col_count):
        widget.grid_columnconfigure(i, weight=1)

    for child in widget.children.values():
        child.grid_configure(sticky="nsew")
        make_dynamic(child)