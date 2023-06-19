import { Box, Button } from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../redux/store";
import ReactFlow, { Edge, OnNodesChange, applyNodeChanges, OnEdgesChange, applyEdgeChanges, Connection, addEdge, Controls, MiniMap, Background } from "reactflow";
import { DiscountNodes } from "../../../types/systemTypes/DiscountNodes";

import 'reactflow/dist/style.css';
import { Node } from 'reactflow';
import Bar2 from "../../Bars/Navbar/NavBar2";
import { addCompositeDiscount, reset } from "../../../reducers/discountSlice";

const CompositeScreen = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const userId = useAppSelector(state => state.auth.userId);
    const storeId = useAppSelector(state => state.store.storeState.watchedStore.storeId);
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    const initialNodes = useAppSelector(state => state.discount.discountNodes);
    const initialEdges = useAppSelector(state => state.discount.discountEdges);
    const [nodes, setNodes] = useState<Node<DiscountNodes>[]>(initialNodes);
    const [edges, setEdges] = useState<Edge[]>(initialEdges);


    const handleRegular = () => {
        console.log('regular');
        navigate('addNewRegular');
    }
    const handleComosite = () => {
        console.log('composite');
        navigate('addNewComposite');
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
    useEffect(() => {

    }, [nodes, edges, initialNodes, initialEdges]);
    return (
        <>
            <Bar2 headLine={`hello ${privateName} , wellcome to `} />
            <Box display='flex'>
                <Button sx={{ mt: 2, mr: 2 }} variant="contained" onClick={handleRegular}>add regular discount</Button>
                <Button sx={{ mt: 2, ml: 2 }} variant="contained" onClick={handleComosite}>add composite discount</Button>
                <Button sx={{ mt: 2, ml: 2 }} variant="contained" onClick={() => {
                    dispatch(reset());
                    setNodes([]);
                    setEdges([]);
                }}>reset</Button>
                <Button sx={{ mt: 2, ml: 2 }} variant="contained" onClick={() => {
                    dispatch(addCompositeDiscount({
                        storeId: storeId,
                        userId: userId,
                        description: nodes[0].data.description,
                        discountNodes: nodes,
                        discountEdges: edges
                    }));

                    dispatch(reset());
                    setNodes([]);
                    setEdges([]);
                    navigate('/dashboard/store/superior');
                }}>submit</Button>
            </Box >
            <div style={{ width: '100vw', height: '100vh' }}>
                <ReactFlow
                    nodes={nodes}
                    edges={edges}
                    onNodesChange={onNodesChange}
                    onEdgesChange={onEdgesChange}
                    onNodeClick={(event, node) => {
                        console.log(event, node);
                        //navigate(`/discounts/${node.data?.id}`);
                    }}
                    onConnect={onConnect}
                >
                    <Controls />
                    <MiniMap />
                    <Background variant={"dots"} gap={12} size={1} />
                </ReactFlow>
            </div>
        </>
    );
}
export default CompositeScreen;
