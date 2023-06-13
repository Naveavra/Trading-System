import { Box, Button } from '@mui/material';
import { useCallback, useState } from 'react';
import ReactFlow, {
    MiniMap,
    Controls,
    Background,
    addEdge,
    Connection,
    Edge,
    NodeTypes,
    NodeProps,
    OnEdgesChange,
    OnNodesChange,
    applyEdgeChanges,
    applyNodeChanges,
    WrapNodeProps,
} from 'reactflow';

import 'reactflow/dist/style.css';
import { Node } from 'reactflow';

import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import { DiscountNodes } from '../../types/systemTypes/DiscountNodes';




export default function App() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const initialNodes = useAppSelector(state => state.discount.discountNodes);
    const initialEdges = useAppSelector(state => state.discount.discountEdges);
    const [nodes, setNodes] = useState<Node<DiscountNodes>[]>(initialNodes);
    const [edges, setEdges] = useState<Edge[]>(initialEdges);


    const handleRegular = () => {
    }
    const handleComosite = () => {
    }
    const onNodesChange: OnNodesChange = useCallback(
        (changes) => setNodes((nds) => applyNodeChanges(changes, nds)),
        [setNodes]
    );
    const onEdgesChange: OnEdgesChange = useCallback(
        (changes) => setEdges((eds) => applyEdgeChanges(changes, eds)),
        [setEdges]
    );

    const onConnect = useCallback((params: Edge | Connection) => setEdges((eds) => addEdge(params, eds)), [setEdges]);
    return (
        <>
            <Box display='flex'>
                <Button sx={{ mt: 2, mr: 2 }} variant="contained" onClick={handleRegular}>add regular discount</Button>
                <Button sx={{ mt: 2, ml: 2 }} variant="contained" onClick={handleComosite}>add composite discount</Button>
            </Box>
            <div style={{ width: '100vw', height: '100vh' }}>
                <ReactFlow
                    nodes={nodes}
                    edges={edges}
                    onNodesChange={onNodesChange}
                    onEdgesChange={onEdgesChange}
                    onNodeClick={(event, node) => {
                        console.log(event, node);
                        navigate(`/discounts/${node.data?.id}`);
                    }}
                    onConnect={onConnect}
                >
                    <Controls />
                    <MiniMap />
                    <Background variant="dots" gap={12} size={1} />
                </ReactFlow>
            </div>
        </>
    );
}