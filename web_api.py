import asyncio
import base64
from flask import Flask, request, jsonify

from aiohttp import ClientSession
from pyopenuv import Client
from pyopenuv.errors import OpenUvError

api_token = "758a878160e1859067e33fb54d8a9797"
async def get_current_uv_related_info(lat, lng, alt, info_type):
    async with ClientSession() as session:
        client = Client(
            api_token,
            lat,
            lng,
            altitude=alt,
            session=session,
        )

        try:
            # Get current UV info:
            if info_type == "current_uv":
                current_uv = await client.uv_index()
                return base64.b64encode(str(current_uv).encode("utf-8"))
            # Get forecasted UV info:
            elif info_type == "forecasted_uv":
                forecasted_uv = await client.uv_forecast()
                return base64.b64encode(str(forecasted_uv).encode("utf-8"))
            # Get UV protection window: 
            elif info_type == "uv_protection_window":
                uv_protection_window = await client.uv_protection_window()
                return base64.b64encode(str(uv_protection_window).encode("utf-8"))
            else:
               return base64.b64encode("{'error': 'wrong type of info request'}".encode("utf-8"))
        except OpenUvError as err:
            return f"There was an error: {err}"


app = Flask(__name__)

@app.route('/')
def hello():
    return "Hello!"

@app.route('/api/get_current_uv_index_info', methods=['POST'])
def current_uv():
    json_data = request.json
    lat = json_data["lat"]
    lng = json_data["lng"] 
    alt = json_data["alt"]
    return asyncio.run(get_current_uv_related_info(lat, lng, alt, "current_uv"))

@app.route('/api/get_forecasted_uv_info', methods=['POST'])
def forecasted_uv():
    json_data = request.json
    lat = json_data["lat"]
    lng = json_data["lng"] 
    alt = json_data["alt"]
    return asyncio.run(get_current_uv_related_info(lat, lng, alt, "forecasted_uv"))

@app.route('/api/get_uv_protection_window', methods=['POST'])
def uv_protection_window():
    json_data = request.json
    lat = json_data["lat"]
    lng = json_data["lng"] 
    alt = json_data["alt"]
    return asyncio.run(get_current_uv_related_info(lat, lng, alt, "uv_protection_window"))

if __name__ == "__main__":
    app.run(threaded=True, host="0.0.0.0", port=11730)