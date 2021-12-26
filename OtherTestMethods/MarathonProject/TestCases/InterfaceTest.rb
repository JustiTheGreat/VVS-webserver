#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test

    with_window("VVS Web Server - [Stopped]") {
        click("Start server")
        select("Web root directory", "C:/Users/justi/Desktop/")
        click("Start server")
        select("Maintenance directory", "C:/Users/justi/Desktop/")
        click("Start server")
    }

    with_window("VVS Web Server - [Running]") {
        click("Stop server")
    }

    with_window("VVS Web Server - [Stopped]") {
        select("Server listening on port", "10,010")
        click("Start server")
    }

    with_window("VVS Web Server - [Running]") {
        select("Maintenance directory", "C:/Users/justi/Desktop/VVS-webserver/")
        select("Switch to maintenance mode", "true")
    }

    with_window("VVS Web Server - [Maintenance]") {
        select("Web root directory", "C:/Users/justi/Desktop/VVS-webserver/")
        select("Switch to maintenance mode", "false")
    }

    with_window("VVS Web Server - [Running]") {
        select("Switch to maintenance mode", "true")
    }

    with_window("VVS Web Server - [Maintenance]") {
        click("Stop server")
    }

end
