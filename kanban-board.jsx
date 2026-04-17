import { useState } from "react";

const TEAM = [
  { id:"fe",   name:"Frontend Dev",     avatar:"FE", color:"#6366f1" },
  { id:"be",   name:"Backend Dev",      avatar:"BE", color:"#10b981" },
  { id:"arch", name:"Tech Lead / Arch", avatar:"TL", color:"#f59e0b" },
  { id:"qa",   name:"QA Engineer",      avatar:"QA", color:"#ef4444" },
];

const STATUS = {
  BACKLOG:     { id:"BACKLOG",     label:"Backlog",     icon:"○",  color:"#94a3b8", bg:"#f1f5f9" },
  TODO:        { id:"TODO",        label:"To Do",       icon:"◎",  color:"#3b82f6", bg:"#dbeafe" },
  IN_PROGRESS: { id:"IN_PROGRESS", label:"In Progress", icon:"⚡", color:"#f59e0b", bg:"#fef3c7" },
  REVIEW:      { id:"REVIEW",      label:"In Review",   icon:"👀", color:"#8b5cf6", bg:"#ede9fe" },
  DONE:        { id:"DONE",        label:"Done",        icon:"✓",  color:"#10b981", bg:"#d1fae5" },
};
const COLS = Object.values(STATUS);

const EPIC = {
  "Infra":      { color:"#6366f1", label:"🏗️ Infra" },
  "API CRUD":   { color:"#10b981", label:"📋 API CRUD" },
  "Versioning": { color:"#f59e0b", label:"🔀 Versioning" },
  "Editor":     { color:"#8b5cf6", label:"✏️ Editor" },
  "Docs":       { color:"#3b82f6", label:"📚 Docs" },
  "Discovery":  { color:"#ec4899", label:"🔍 Discovery" },
  "UI/UX":      { color:"#14b8a6", label:"🎨 UI/UX" },
  "QA":         { color:"#ef4444", label:"🧪 QA" },
};
const PRIO_COLOR = { HIGH:"#ef4444", MEDIUM:"#f59e0b", LOW:"#94a3b8" };

const SP_STATUS = {
  DONE:        { label:"✅ Completato",          color:"#10b981", bg:"#d1fae5", border:"#10b981" },
  IN_PROGRESS: { label:"⚡ In corso",            color:"#f59e0b", bg:"#fef3c7", border:"#f59e0b" },
  AWAITING:    { label:"⏳ Attesa approvazione", color:"#94a3b8", bg:"#f8fafc", border:"#e2e8f0" },
};

// IMPORTANTE: ogni story ha UN status esplicito — appare in UNA sola colonna
const SPRINTS = [
  {
    id:0, name:"Sprint 0", fullName:"Sprint 0 — Setup & Architettura",
    goal:"Bootstrap del progetto, struttura repo, schema DB, CI/CD",
    dates:"11–12 Apr 2026", sprintStatus:"DONE",
    stories:[
      { id:"S0-1", title:"Setup repository GitHub + struttura monorepo",  points:2, assignee:"arch", epic:"Infra", priority:"HIGH",   status:"DONE",
        desc:"Repo GitHub creato, struttura /frontend e /backend, .gitignore, README" },
      { id:"S0-2", title:"Bootstrap React 18 + Vite 5 + Ant Design 5.x", points:3, assignee:"fe",   epic:"Infra", priority:"HIGH",   status:"DONE",
        desc:"Routing React Router 6, layout shell, pagine Dashboard/ApiList/ApiDetail" },
      { id:"S0-3", title:"Bootstrap Spring Boot 3.2.5 + H2 + JPA",       points:3, assignee:"be",   epic:"Infra", priority:"HIGH",   status:"DONE",
        desc:"Spring Data JPA, H2 in-memory, CORS, 5 API di esempio nel DB" },
      { id:"S0-4", title:"Schema DB e contratti API REST",                 points:2, assignee:"arch", epic:"Infra", priority:"HIGH",   status:"DONE",
        desc:"Entità ApiDefinition, ApiVersion, Enum, DTO, 9 endpoint documentati" },
      { id:"S0-5", title:"CI/CD GitHub Actions + push su GitHub",         points:1, assignee:"arch", epic:"Infra", priority:"MEDIUM", status:"DONE",
        desc:"frontend-ci.yml, backend-ci.yml, push su antocecere77/swagger-hub-clone" },
    ]
  },
  {
    id:1, name:"Sprint 1", fullName:"Sprint 1 — Core API Management (Backend)",
    goal:"CRUD completo API, versioning base, endpoint REST testabili con paginazione",
    dates:"11–30 Apr 2026", sprintStatus:"IN_PROGRESS",
    stories:[
      { id:"S1-1", title:"Entità ApiDefinition + repository JPA",         points:3, assignee:"be", epic:"API CRUD",   priority:"HIGH",   status:"IN_PROGRESS",
        desc:"Entità con tutti i campi, query custom, paginazione. Avviato oggi." },
      { id:"S1-2", title:"GET /apis — lista con filtri e paginazione",    points:3, assignee:"be", epic:"API CRUD",   priority:"HIGH",   status:"TODO",
        desc:"Paginazione server-side, filtro nome/categoria/tag, ordinamento" },
      { id:"S1-3", title:"POST /apis — creazione nuova API",              points:2, assignee:"be", epic:"API CRUD",   priority:"HIGH",   status:"TODO",
        desc:"Bean Validation, response 201 con Location header" },
      { id:"S1-4", title:"GET / PUT / DELETE /apis/{id}",                 points:3, assignee:"be", epic:"API CRUD",   priority:"HIGH",   status:"TODO",
        desc:"CRUD completo, gestione 404/400, soft delete via status=DELETED" },
      { id:"S1-5", title:"Entità ApiVersion + CRUD versioni",             points:3, assignee:"be", epic:"Versioning", priority:"HIGH",   status:"TODO",
        desc:"Versioni con spec YAML/JSON, stati DRAFT/PUBLISHED/DEPRECATED" },
      { id:"S1-6", title:"Upload e storage spec YAML/JSON",               points:3, assignee:"be", epic:"API CRUD",   priority:"HIGH",   status:"TODO",
        desc:"Salvataggio spec come TEXT in DB, validazione formato, parsing base" },
      { id:"S1-7", title:"Test unitari e integration test backend",        points:2, assignee:"qa", epic:"QA",         priority:"MEDIUM", status:"TODO",
        desc:"JUnit 5, MockMvc, test endpoint principali, coverage > 70%" },
    ]
  },
  {
    id:2, name:"Sprint 2", fullName:"Sprint 2 — Dashboard & API List (Frontend)",
    goal:"Dashboard funzionale, lista API, CRUD da UI, React Query",
    dates:"4–15 Mag 2026", sprintStatus:"AWAITING",
    stories:[
      { id:"S2-1", title:"Layout principale: sidebar, header, content",   points:3, assignee:"fe", epic:"UI/UX",    priority:"HIGH",   status:"BACKLOG", desc:"Sider navigazione, header con search bar, layout responsive" },
      { id:"S2-2", title:"Dashboard home: statistiche e API recenti",     points:3, assignee:"fe", epic:"UI/UX",    priority:"HIGH",   status:"BACKLOG", desc:"Cards contatori, lista ultime API modificate" },
      { id:"S2-3", title:"API List: tabella filtri, ricerca, paginazione",points:5, assignee:"fe", epic:"API CRUD", priority:"HIGH",   status:"BACKLOG", desc:"Ant Design Table, filtri categoria, search, paginazione server-side" },
      { id:"S2-4", title:"Modal Crea/Modifica API",                       points:3, assignee:"fe", epic:"API CRUD", priority:"HIGH",   status:"BACKLOG", desc:"Form nome, descrizione, categoria, visibilità, tag + validazione" },
      { id:"S2-5", title:"Confirm eliminazione + loading/error states",   points:2, assignee:"fe", epic:"API CRUD", priority:"MEDIUM", status:"BACKLOG", desc:"Confirm modal, skeleton loading, toast notifications" },
      { id:"S2-6", title:"Integrazione Axios + React Query",              points:3, assignee:"fe", epic:"Infra",    priority:"HIGH",   status:"BACKLOG", desc:"Axios base URL, React Query cache e invalidazione, custom hooks" },
    ]
  },
  {
    id:3, name:"Sprint 3", fullName:"Sprint 3 — OpenAPI Editor",
    goal:"Monaco Editor YAML/JSON, validazione real-time, split view",
    dates:"18–29 Mag 2026", sprintStatus:"AWAITING",
    stories:[
      { id:"S3-1", title:"Monaco Editor per YAML/JSON",                   points:5, assignee:"fe", epic:"Editor",    priority:"HIGH",   status:"BACKLOG", desc:"Syntax highlight, theme, shortcuts" },
      { id:"S3-2", title:"Parser e validazione OpenAPI 3.x real-time",    points:5, assignee:"fe", epic:"Editor",    priority:"HIGH",   status:"BACKLOG", desc:"openapi-parser, errori inline, pannello errori" },
      { id:"S3-3", title:"Split view Editor + Preview",                   points:3, assignee:"fe", epic:"Editor",    priority:"HIGH",   status:"BACKLOG", desc:"Layout resizable, sincronizzazione real-time" },
      { id:"S3-4", title:"Tree view struttura spec",                      points:3, assignee:"fe", epic:"Editor",    priority:"MEDIUM", status:"BACKLOG", desc:"Navigazione ad albero paths/schemas/components" },
      { id:"S3-5", title:"Salvataggio spec con versioning",               points:3, assignee:"fe", epic:"Versioning",priority:"HIGH",   status:"BACKLOG", desc:"Save, scelta versione, cambio stato DRAFT→PUBLISHED" },
      { id:"S3-6", title:"Validazione spec lato backend",                 points:3, assignee:"be", epic:"Editor",    priority:"MEDIUM", status:"BACKLOG", desc:"POST /apis/{id}/validate, lista errori strutturata" },
    ]
  },
  {
    id:4, name:"Sprint 4", fullName:"Sprint 4 — Swagger UI & Try It Out",
    goal:"Documentazione interattiva, try-it-out, export/import spec",
    dates:"1–12 Giu 2026", sprintStatus:"AWAITING",
    stories:[
      { id:"S4-1", title:"Swagger UI React embedded",                     points:5, assignee:"fe", epic:"Docs", priority:"HIGH",   status:"BACKLOG", desc:"swagger-ui-react, rendering spec, tab Editor/Docs" },
      { id:"S4-2", title:"Try It Out: richieste HTTP dal browser",        points:5, assignee:"fe", epic:"Docs", priority:"HIGH",   status:"BACKLOG", desc:"Form params, headers, body, response con syntax highlight" },
      { id:"S4-3", title:"Backend proxy per Try It Out",                  points:3, assignee:"be", epic:"Docs", priority:"HIGH",   status:"BACKLOG", desc:"POST /proxy, forwarding richieste, gestione headers/timeout" },
      { id:"S4-4", title:"Pagina pubblica API (link shareable)",          points:3, assignee:"fe", epic:"Docs", priority:"MEDIUM", status:"BACKLOG", desc:"Route /apis/{id}/docs pubblica, Swagger UI read-only" },
      { id:"S4-5", title:"Export spec: download YAML/JSON",               points:2, assignee:"fe", epic:"Docs", priority:"MEDIUM", status:"BACKLOG", desc:"Bottone Export, scelta formato, download file" },
      { id:"S4-6", title:"Import spec da file o URL",                     points:3, assignee:"fe", epic:"Docs", priority:"MEDIUM", status:"BACKLOG", desc:"Upload file YAML/JSON, import da URL, caricamento editor" },
    ]
  },
  {
    id:5, name:"Sprint 5", fullName:"Sprint 5 — Polishing, Search & Deploy",
    goal:"Ricerca globale, tag, version diff, dark mode, Docker",
    dates:"15–26 Giu 2026", sprintStatus:"AWAITING",
    stories:[
      { id:"S5-1", title:"Ricerca globale con highlighting",              points:3, assignee:"fe",   epic:"Discovery",  priority:"HIGH",   status:"BACKLOG", desc:"Search nome/descrizione/tag, search-as-you-type" },
      { id:"S5-2", title:"Gestione Tag: creazione, assegnazione, filtro",points:3, assignee:"fe",   epic:"Discovery",  priority:"MEDIUM", status:"BACKLOG", desc:"Tag colorate, autocomplete, filtro lista" },
      { id:"S5-3", title:"History versioni: timeline e confronto diff",   points:5, assignee:"fe",   epic:"Versioning", priority:"MEDIUM", status:"BACKLOG", desc:"Timeline, Monaco diff editor, restore version" },
      { id:"S5-4", title:"Dark mode e tema personalizzabile",             points:2, assignee:"fe",   epic:"UI/UX",      priority:"LOW",    status:"BACKLOG", desc:"Toggle dark/light, persistenza, Ant Design tokens" },
      { id:"S5-5", title:"Test E2E con Playwright",                       points:3, assignee:"qa",   epic:"QA",         priority:"HIGH",   status:"BACKLOG", desc:"Scenari: crea API, modifica spec, pubblica, visualizza docs" },
      { id:"S5-6", title:"Build produzione + Docker Compose",             points:3, assignee:"arch", epic:"Infra",      priority:"HIGH",   status:"BACKLOG", desc:"Dockerfile FE/BE, docker-compose.yml, variabili env" },
    ]
  },
];

/* ── helpers ── */
function Avatar({ id, size=22 }) {
  const m = TEAM.find(t=>t.id===id);
  if(!m) return null;
  return <span title={m.name} style={{display:"inline-flex",alignItems:"center",justifyContent:"center",width:size,height:size,borderRadius:"50%",background:m.color,color:"#fff",fontSize:size*0.42,fontWeight:700,flexShrink:0}}>{m.avatar}</span>;
}
function StatusPill({ sid }) {
  const s=STATUS[sid]; if(!s) return null;
  return <span style={{background:s.bg,color:s.color,borderRadius:4,padding:"1px 7px",fontSize:10,fontWeight:700,whiteSpace:"nowrap"}}>{s.icon} {s.label}</span>;
}
function EpicTag({ epic }) {
  const e=EPIC[epic]||{color:"#999",label:epic};
  return <span style={{background:e.color+"22",color:e.color,borderRadius:4,padding:"1px 6px",fontSize:10,fontWeight:600}}>{e.label}</span>;
}
function Pts({ n }) {
  return <span style={{background:"#e2e8f0",borderRadius:"50%",width:20,height:20,display:"flex",alignItems:"center",justifyContent:"center",fontSize:10,fontWeight:700,color:"#475569",flexShrink:0}}>{n}</span>;
}

/* ── Kanban card ── */
function Card({ s }) {
  return (
    <div style={{background:"#fff",borderRadius:8,padding:"10px 12px",marginBottom:8,boxShadow:"0 1px 3px rgba(0,0,0,.1)",borderLeft:`3px solid ${PRIO_COLOR[s.priority]}`}}>
      <div style={{display:"flex",gap:6,alignItems:"flex-start"}}>
        <span style={{flex:1,fontWeight:600,color:"#1e293b",fontSize:12,lineHeight:1.4}}>{s.title}</span>
        <Avatar id={s.assignee}/>
      </div>
      <div style={{color:"#64748b",fontSize:11,marginTop:5,lineHeight:1.4}}>{s.desc}</div>
      <div style={{display:"flex",justifyContent:"space-between",alignItems:"center",marginTop:8}}>
        <EpicTag epic={s.epic}/>
        <div style={{display:"flex",gap:5,alignItems:"center"}}>
          <span style={{color:"#94a3b8",fontSize:10}}>{s.id}</span>
          <Pts n={s.points}/>
        </div>
      </div>
    </div>
  );
}

/* ── Kanban board: TUTTE le story, SEMPRE visibili nella loro colonna ── */
function KanbanBoard({ sprint }) {
  const sm = SP_STATUS[sprint.sprintStatus];
  const donePts = sprint.stories.filter(s=>s.status==="DONE").reduce((a,s)=>a+s.points,0);
  const totalPts = sprint.stories.reduce((a,s)=>a+s.points,0);
  return (
    <div>
      <div style={{background:sm.bg,border:`1px solid ${sm.border}66`,borderRadius:8,padding:"10px 16px",marginBottom:14,display:"flex",justifyContent:"space-between",alignItems:"center"}}>
        <div>
          <span style={{fontWeight:700,color:"#1e293b",fontSize:14}}>{sprint.fullName}</span>
          <span style={{marginLeft:10,color:"#64748b",fontSize:12}}>📅 {sprint.dates}</span>
          <div style={{color:"#64748b",fontSize:12,marginTop:2}}>🎯 {sprint.goal}</div>
        </div>
        <div style={{display:"flex",gap:8}}>
          <span style={{background:sm.bg,color:sm.color,border:`1px solid ${sm.border}`,borderRadius:6,padding:"3px 10px",fontSize:11,fontWeight:700}}>{sm.label}</span>
          <span style={{background:"#3b82f620",color:"#3b82f6",borderRadius:6,padding:"3px 10px",fontSize:11,fontWeight:600}}>✅ {donePts}/{totalPts} SP</span>
        </div>
      </div>
      {/* colonne — sempre tutte e 5, con contatore e card */}
      <div style={{display:"flex",gap:10,overflowX:"auto",paddingBottom:8,alignItems:"flex-start"}}>
        {COLS.map(col=>{
          const stories = sprint.stories.filter(s=>s.status===col.id);
          return (
            <div key={col.id} style={{minWidth:215,flex:"0 0 215px"}}>
              <div style={{display:"flex",alignItems:"center",gap:6,padding:"6px 10px",borderRadius:6,background:col.color+"18",marginBottom:8}}>
                <span style={{fontWeight:700,color:col.color,fontSize:12}}>{col.icon} {col.label}</span>
                <span style={{background:col.color,color:"#fff",borderRadius:"50%",width:18,height:18,display:"flex",alignItems:"center",justifyContent:"center",fontSize:10,fontWeight:700}}>{stories.length}</span>
              </div>
              <div style={{minHeight:40}}>
                {stories.length===0
                  ? <div style={{color:"#cbd5e1",fontSize:11,textAlign:"center",padding:"20px 0"}}>—</div>
                  : stories.map(s=><Card key={s.id} s={s}/>)
                }
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

/* ── Chi fa cosa ── */
function TeamView() {
  const all = SPRINTS.flatMap(sp=>sp.stories.map(s=>({...s,sprintName:sp.name})));
  return (
    <div style={{display:"flex",flexDirection:"column",gap:14}}>
      {TEAM.map(m=>{
        const mine=all.filter(s=>s.assignee===m.id);
        return (
          <div key={m.id} style={{background:"#fff",border:`2px solid ${m.color}33`,borderRadius:10,overflow:"hidden"}}>
            <div style={{background:m.color+"18",padding:"10px 16px",display:"flex",alignItems:"center",gap:10,borderBottom:`1px solid ${m.color}33`}}>
              <Avatar id={m.id} size={34}/>
              <div style={{flex:1}}>
                <div style={{fontWeight:700,color:"#1e293b",fontSize:14}}>{m.name}</div>
                <div style={{fontSize:11,color:"#64748b",marginTop:1}}>
                  ⚡ {mine.filter(s=>s.status==="IN_PROGRESS").length} in progress &nbsp;·&nbsp;
                  📌 {mine.filter(s=>s.status==="TODO").length} to do &nbsp;·&nbsp;
                  ✅ {mine.filter(s=>s.status==="DONE").length} done &nbsp;·&nbsp;
                  ○ {mine.filter(s=>s.status==="BACKLOG").length} in backlog
                </div>
              </div>
            </div>
            <div style={{padding:"10px 16px",display:"flex",flexDirection:"column",gap:5}}>
              {mine.length===0
                ? <span style={{color:"#94a3b8",fontSize:12}}>Nessuna story assegnata</span>
                : mine.map(s=>(
                  <div key={s.id} style={{display:"flex",alignItems:"center",gap:8,padding:"6px 10px",borderRadius:6,background:"#f8fafc",border:s.status==="IN_PROGRESS"?`1px solid ${m.color}88`:"1px solid #f1f5f9"}}>
                    <span style={{color:"#94a3b8",fontSize:10,width:44,flexShrink:0}}>{s.id}</span>
                    <span style={{flex:1,fontSize:12,color:"#334155"}}>{s.title}</span>
                    <span style={{fontSize:10,color:"#94a3b8",whiteSpace:"nowrap"}}>{s.sprintName}</span>
                    <StatusPill sid={s.status}/>
                    <EpicTag epic={s.epic}/>
                    <Pts n={s.points}/>
                  </div>
                ))
              }
            </div>
          </div>
        );
      })}
    </div>
  );
}

/* ── Backlog completo ── */
function BacklogView() {
  const total = SPRINTS.flatMap(s=>s.stories);
  return (
    <div style={{display:"flex",flexDirection:"column",gap:12}}>
      {SPRINTS.map(sprint=>{
        const sm=SP_STATUS[sprint.sprintStatus];
        const pts=sprint.stories.reduce((a,s)=>a+s.points,0);
        const dp=sprint.stories.filter(s=>s.status==="DONE").reduce((a,s)=>a+s.points,0);
        return (
          <div key={sprint.id} style={{background:"#fff",border:`1px solid ${sm.border}44`,borderRadius:10,overflow:"hidden"}}>
            <div style={{background:sm.bg,padding:"9px 16px",display:"flex",justifyContent:"space-between",alignItems:"center",borderBottom:`1px solid ${sm.border}44`}}>
              <div>
                <span style={{fontWeight:700,color:"#1e293b",fontSize:13}}>{sprint.fullName}</span>
                <span style={{marginLeft:10,fontSize:11,color:"#64748b"}}>📅 {sprint.dates}</span>
              </div>
              <div style={{display:"flex",gap:8}}>
                <span style={{background:sm.bg,color:sm.color,border:`1px solid ${sm.border}`,borderRadius:6,padding:"2px 10px",fontSize:11,fontWeight:700}}>{sm.label}</span>
                <span style={{background:"#3b82f620",color:"#3b82f6",borderRadius:6,padding:"2px 8px",fontSize:11,fontWeight:600}}>✅ {dp}/{pts} SP</span>
              </div>
            </div>
            <div style={{padding:"8px 16px",display:"flex",flexDirection:"column",gap:5}}>
              {sprint.stories.map(s=>(
                <div key={s.id} style={{display:"flex",alignItems:"center",gap:8,padding:"6px 10px",borderRadius:6,background:"#f8fafc",border:"1px solid #f1f5f9"}}>
                  <span style={{color:"#94a3b8",fontSize:10,width:44,flexShrink:0}}>{s.id}</span>
                  <span style={{flex:1,fontSize:12,color:"#334155"}}>{s.title}</span>
                  <Avatar id={s.assignee}/>
                  <EpicTag epic={s.epic}/>
                  <span style={{background:PRIO_COLOR[s.priority]+"22",color:PRIO_COLOR[s.priority],borderRadius:3,padding:"1px 5px",fontSize:9,fontWeight:700}}>{s.priority}</span>
                  <StatusPill sid={s.status}/>
                  <Pts n={s.points}/>
                </div>
              ))}
            </div>
          </div>
        );
      })}
    </div>
  );
}

/* ── ROOT ── */
export default function App() {
  const [view,setView] = useState("kanban");
  const [sprintId,setSprintId] = useState(1);
  const all = SPRINTS.flatMap(s=>s.stories);
  const donePts = all.filter(s=>s.status==="DONE").reduce((a,s)=>a+s.points,0);
  const totalPts = all.reduce((a,s)=>a+s.points,0);
  const activeSp = SPRINTS.find(s=>s.id===sprintId);
  const Tab=({id,label})=>(
    <button onClick={()=>setView(id)} style={{padding:"7px 18px",borderRadius:8,border:"none",cursor:"pointer",fontWeight:600,fontSize:13,background:view===id?"#3b82f6":"#f1f5f9",color:view===id?"#fff":"#475569",transition:"all .15s"}}>{label}</button>
  );
  return (
    <div style={{minHeight:"100vh",background:"#f8fafc",fontFamily:"'Inter',system-ui,sans-serif"}}>
      {/* HEADER */}
      <div style={{background:"linear-gradient(135deg,#0f172a,#1e293b)",padding:"18px 24px"}}>
        <div style={{display:"flex",justifyContent:"space-between",alignItems:"center"}}>
          <div>
            <div style={{fontSize:20,fontWeight:800,color:"#fff",letterSpacing:"-.5px"}}>🔷 SwaggerHub Clone — Project Board</div>
            <div style={{color:"#64748b",fontSize:12,marginTop:3}}>React + Ant Design &nbsp;|&nbsp; Spring Boot 3.x &nbsp;|&nbsp; Agile Sprints</div>
          </div>
          <div style={{display:"flex",gap:8}}>
            {TEAM.map(m=>(
              <div key={m.id} style={{display:"flex",alignItems:"center",gap:6,background:"#ffffff18",borderRadius:20,padding:"4px 12px"}}>
                <Avatar id={m.id}/><span style={{fontSize:11,color:"#cbd5e1"}}>{m.name}</span>
              </div>
            ))}
          </div>
        </div>
        <div style={{display:"flex",gap:12,marginTop:14,flexWrap:"wrap"}}>
          {[
            {icon:"⚡",label:"Sprint attivo",  value:"Sprint 1"},
            {icon:"🔨",label:"In lavorazione", value:`${all.filter(s=>s.status==="IN_PROGRESS").length} story`},
            {icon:"✅",label:"SP completati",  value:`${donePts}/${totalPts}`},
            {icon:"📅",label:"Fine sprint",    value:"30 Apr 2026"},
            {icon:"🔗",label:"GitHub",         value:"✅ Pushato"},
          ].map(k=>(
            <div key={k.label} style={{background:"#ffffff12",borderRadius:8,padding:"8px 14px",display:"flex",alignItems:"center",gap:8}}>
              <span style={{fontSize:16}}>{k.icon}</span>
              <div>
                <div style={{color:"#fff",fontWeight:700,fontSize:15}}>{k.value}</div>
                <div style={{color:"#64748b",fontSize:10}}>{k.label}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
      {/* TABS */}
      <div style={{background:"#fff",padding:"12px 24px 0",display:"flex",gap:8,borderBottom:"1px solid #e2e8f0"}}>
        <Tab id="kanban"  label="🗂️ Kanban"/>
        <Tab id="team"    label="👥 Chi fa cosa"/>
        <Tab id="backlog" label="📋 Backlog completo"/>
      </div>
      {/* CONTENT */}
      <div style={{padding:"20px 24px"}}>
        {view==="kanban" && (
          <div>
            <div style={{display:"flex",gap:8,marginBottom:14,alignItems:"center",flexWrap:"wrap"}}>
              <span style={{fontSize:12,color:"#64748b",fontWeight:600}}>Sprint:</span>
              {SPRINTS.map(sp=>{
                const sm=SP_STATUS[sp.sprintStatus]; const active=sp.id===sprintId;
                return (
                  <button key={sp.id} onClick={()=>setSprintId(sp.id)} style={{padding:"5px 14px",borderRadius:8,cursor:"pointer",fontWeight:600,fontSize:12,border:active?`2px solid ${sm.color}`:"2px solid transparent",background:active?sm.bg:"#f1f5f9",color:active?sm.color:"#64748b"}}>
                    {sp.name} {sp.sprintStatus==="IN_PROGRESS"?"⚡":sp.sprintStatus==="DONE"?"✅":"⏳"} <span style={{fontSize:10,opacity:.7}}>({sp.stories.filter(s=>s.status==="DONE").length}/{sp.stories.length})</span>
                  </button>
                );
              })}
            </div>
            {activeSp && <KanbanBoard sprint={activeSp}/>}
          </div>
        )}
        {view==="team"    && <TeamView/>}
        {view==="backlog" && <BacklogView/>}
      </div>
    </div>
  );
}
