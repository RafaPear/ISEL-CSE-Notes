import sys

def main(args) -> None:
    
    if len(args) <= 0:
        print("!!! No Arguments provided !!!")
        print("Usage: python main.py [options]")
        print("Options:")
        print("  -h, --help    Show this help message and exit")
        print("  -v, --version Show version information")
        print("  -g, --gui     Start GUI")
        print("  -c, --cli     Start CLI")
        return
    
    for arg in args:
        if arg == "-h" or arg == "--help":
            print("Usage: python main.py [options]")
            print("Options:")
            print("  -h, --help    Show this help message and exit")
            print("  -v, --version Show version information")
            print("  -g, --gui     Start GUI")
            print("  -c, --cli     Start CLI")
            return
        elif arg == "-v" or arg == "--version":
            print("Version 0.1.2")
            return
        elif arg == '--cli' or arg == '-c':
            from web_client import web_client
            print("Starting CLI")
            wc = web_client()
            wc.ask_input()
            break
        elif arg == '--gui' or arg == '-g':
            from gui import Gui
            print("Starting GUI")
            Gui().init()
            break
        else:
            print("Unknown argument:", arg)
            print("Use -h or --help for help")
            return

if __name__ == '__main__':
    main(args=sys.argv[1:])