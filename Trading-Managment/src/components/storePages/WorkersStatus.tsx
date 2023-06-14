import { Typography, Box, Card, CardContent, CardActions } from "@mui/material";
import Bar2 from "../Bars/Navbar/NavBar2";
import orders from "./StoreOrders";
import { useAppSelector } from "../../redux/store";
import { PersonInfo } from "../../types/systemTypes/PersonInfo";

const WorkersStatus = () => {
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    let list: PersonInfo[] = [];
    let realList: PersonInfo[] = [];
    debugger;
    const workers = store.appHistory.forEach((person) => {
        list.push(person.memane);
        person.memoonim.forEach((worker) => {
            list.push(worker);
        });
    });
    list.forEach(element => {
        let esixt = false;
        realList.forEach((person) => {
            if (person.userId === element.userId) {
                esixt = true;
            }
        });
        if (!esixt) {
            realList.push(element);
        }
    });
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    console.log(orders);
    return (
        <>
            <Bar2 headLine={`hello ${privateName} , wellcome to `} />
            <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                workers details
            </Typography >
            <Box sx={{ display: "flex", width: '100%', mb: 2 }}>
                {
                    realList?.map((person, index) => {
                        return (
                            <Card sx={{ width: 350, mt: 5, ml: 3 }} key={index}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        role in store:  {person.role}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        userId:  {person.userId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        name:  {person.email}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        age:  {person.age}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        birthday:  {person.birthday}
                                    </Typography>
                                    <>
                                        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                            permissions:
                                        </Typography>
                                        {person.managerPermissions.map((permission, index) => {
                                            return (
                                                <Typography sx={{ fontSize: 14, ml: 10 }} color="text.secondary" gutterBottom>
                                                    {permission}
                                                </Typography>
                                            );
                                        }
                                        )}
                                    </>
                                </CardContent>

                                <CardActions>
                                    {/* <IconButton onClick={() => handleClickOrder(order.orderId)}>
                                    <InfoIcon />
                                </IconButton>
                                <IconButton onClick={() => navigate(`${order.orderId}/sendComplaint`)}>
                                    <RateReviewIcon />
                                </IconButton> */}
                                </CardActions>
                            </Card>
                        )
                    })
                }
            </Box>
        </>
    );
}
export default WorkersStatus;