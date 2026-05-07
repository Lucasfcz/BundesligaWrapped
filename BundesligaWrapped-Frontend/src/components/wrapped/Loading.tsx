import { useEffect, useState } from "react";
import { ParticleCanvas } from "./Effects";

const MESSAGES = [
  "TUNING THE STADIUM SPEAKERS",
  "COUNTING YOUR GOALS",
  "REWINDING THE HIGHLIGHTS",
  "POLISHING THE TROPHY",
  "CHECKING VAR ONE LAST TIME",
  "ASSEMBLING YOUR SEASON",
];

export function Loading() {
  const [i, setI] = useState(0);
  useEffect(() => {
    const id = window.setInterval(() => setI((v) => (v + 1) % MESSAGES.length), 1100);
    return () => clearInterval(id);
  }, []);
  return (
    <div className="bw-loading bw-app">
      <ParticleCanvas color="#D4001A" count={40} />
      <div className="bw-load-title">BUNDESLIGA WRAPPED</div>
      <div className="bw-rings">
        <div className="bw-ring" />
        <div className="bw-ring bw-ring-2" />
        <div className="bw-ring bw-ring-3" />
        <div className="bw-load-core">⚽</div>
      </div>
      <div key={i} className="bw-load-msg">
        {MESSAGES[i]}
      </div>
      <div className="bw-load-bar-wrap">
        <div className="bw-load-bar" />
      </div>
    </div>
  );
}