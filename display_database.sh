PORT=8000
ADMINER_PATH="adminer/index.php"
URL="http://127.0.0.1:$PORT"

PIDS=$(lsof -t -i :$PORT)

if [ ! -z "$PIDS" ]; then
    kill -9 $PIDS
    echo "Closed ports $PORT"
else
    echo "Ports $PORT are not in use"
fi



# Start the PHP server in the background
echo "Starting PHP server..."
php -S 127.0.0.1:$PORT -t "$(dirname "$ADMINER_PATH")" &
SERVER_PID=$!
echo "Server started at $URL"


# Wait a bit to ensure the server is up
sleep 2

# Open Adminer in the default browser
echo "Opening Adminer in the browser..."
if command -v xdg-open > /dev/null; then
  xdg-open "$URL" #for linux
elif command -v open > /dev/null; then
  open "$URL" #for mac
elif command -v start > /dev/null; then
  start "$URL" #for windows
else
  echo "No URL opening utility found."
fi

# Wait for the PHP server to finish (optional, if you want to keep the script running until the server stops)
wait $SERVER_PID