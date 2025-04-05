param (
    [string]$action = "start"  # Options: start | stop | clean
)

function Build-Shortener {
    Write-Host ">>> Building Maven project..."
    Push-Location .\shorten-url-service
    mvn clean install
    Pop-Location

    Write-Host ">>> Building Docker images..."
    docker compose build
}

function Run-Shortener {
    Build-Shortener
    Write-Host ">>> Starting services..."
    docker compose up
}

function Stop-Shortener {
    Write-Host ">>> Stopping containers..."
    docker compose down
}

function Clean-Shortener {
    Write-Host ">>> Cleaning containers, volumes, images..."
    docker compose down -v --remove-orphans
    docker image prune -f
    docker volume prune -f
}

switch ($action.ToLower()) {
    "start" { Run-Shortener }
    "stop"  { Stop-Shortener }
    "clean" { Clean-Shortener }
    default {
        Write-Host "Invalid action. Use: start | stop | clean"
    }
}
