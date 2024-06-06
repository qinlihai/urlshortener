import React, { useState } from 'react';
import axios from 'axios';

const App = () => {
  const [originalUrl, setOriginalUrl] = useState('');
  const [shortenedUrl, setShortenedUrl] = useState('');
  const [shortCode, setShortCode] = useState('');
  const [statistics, setStatistics] = useState(null);

  const handleShortenUrl = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/v1/shorten', originalUrl, {
        headers: { 'Content-Type': 'text/plain' }
      });
      setShortenedUrl(response.data.shortUrl);
    } catch (error) {
      console.error('Error shortening URL:', error);
    }
  };

  const handleGetStatistics = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/api/v1/urlstatistics/${shortCode}`);
      setStatistics(response.data);
    } catch (error) {
      console.error('Error retrieving statistics:', error);
    }
  };

  return (
      <div>
        <h1>URL Shortener</h1>
        <div>
          <h2>Shorten URL</h2>
          <input
              type="text"
              value={originalUrl}
              onChange={(e) => setOriginalUrl(e.target.value)}
              placeholder="Enter URL"
          />
          <button onClick={handleShortenUrl}>Shorten</button>
          {shortenedUrl && (
              <div>
                <p>Shortened URL: <a href={shortenedUrl} target="_blank" rel="noopener noreferrer">{shortenedUrl}</a></p>
              </div>
          )}
        </div>
        <div>
          <h2>Get URL Statistics</h2>
          <input
              type="text"
              value={shortCode}
              onChange={(e) => setShortCode(e.target.value)}
              placeholder="Enter Short Code"
          />
          <button onClick={handleGetStatistics}>Get Statistics</button>
          {statistics && (
              <div>
                <p>Short Code: {statistics.shortCode}</p>
                <p>Click Count: {statistics.clickCount}</p>
              </div>
          )}
        </div>
      </div>
  );
};

export default App;
